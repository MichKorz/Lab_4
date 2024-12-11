import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private static Server instance;
    ExecutorService pool;
    public int port;

    private Server(int port, int playersCount)
    {
        this.port = port;
        pool = Executors.newFixedThreadPool(playersCount);
        System.out.println("The chat server is running...");
    }

    public static Server getInstance(int port, int playersCount)
    {
        if (instance == null)
        {
            instance = new Server(port, playersCount);
        }
        return instance;
    }

    Game game;

    // Game variant setup
    String Setup()
    {
        game = new GameClassic();
        return "Setup successful";
    }

    public boolean isRunning;
    GameState gameState;


    void Run()
    {
        isRunning = true;
        gameState = new AwaitPlayers(this);

        while(isRunning)
        {
            gameState.stateLoop();
        }
    }
}
