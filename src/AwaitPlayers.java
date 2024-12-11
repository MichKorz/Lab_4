import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AwaitPlayers implements GameState
{
    Server server;
    ExecutorService pool;
    int port;
    int playerCount = 0;
    int targetCount;

    public AwaitPlayers(Server server, int targetCount, int port)
    {
        this.server = server;
        pool = Executors.newFixedThreadPool(targetCount);
        this.port = port;
        this.targetCount = targetCount;
    }

    public void stateLoop()
    {
        try (var listener = new ServerSocket(port))
        {
            while (true)
            {
                Player player = new Player(listener.accept(), new PipedOutputStream(server.moveInputStream));
                server.players.add(player);
                pool.execute(player);
                playerCount++;
                System.out.println("Added player " + playerCount + "/" + targetCount);
                if (playerCount == targetCount)
                {
                    endState();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void endState()
    {
        System.out.println("Entering main game loop");
        server.gameState = new GameLoop();
    }
}
