import java.io.IOException;
import java.net.ServerSocket;

public class AwaitPlayers implements GameState
{
    Server server;

    public AwaitPlayers(Server server)
    {
        this.server = server;
    }

    public void stateLoop()
    {
        try (var listener = new ServerSocket(server.port))
        {
            while (true)
            {
                pool.execute(new Player(listener.accept()));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
