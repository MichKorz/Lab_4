package server;

import java.io.IOException;
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
            System.out.println("Server started on IP: " + listener.getInetAddress().getHostAddress());
            System.out.println("Listening on port: " + listener.getLocalPort());
            while (true)
            {
                String initialMessage = server.gameVariant + " " + server.playerCount;
                System.out.println("(Debug print) initialMessage: " + initialMessage);
                Player player = new Player(server, listener.accept(), server.getQueue(), initialMessage);
                server.getPlayerList().add(player);
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
        server.ChangeState(new GameLoop(this.server));
    }
}
