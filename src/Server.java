import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server
{
    private static Server instance;
    int playerCount;
    private final int port;

    private Server(int port, int playerCount)
    {
        this.port = port;
        this.playerCount = playerCount;
    }

    public static Server getInstance(int port, int playersCount)
    {
        if (instance == null)
        {
            instance = new Server(port, playersCount);
        }
        return instance;
    }

    public Game game;

    // Game variant setup
    void Setup()
    {
        game = new GameClassic();
    }

    public boolean isRunning;
    GameState gameState;
    BlockingQueue<String> moveInputStream;
    public List<Player> players;


    void Run()
    {
        isRunning = true;
        gameState = LaunchNewGame();

        while(isRunning)
        {
            gameState.stateLoop();
        }
    }

    public GameState LaunchNewGame()
    {
        moveInputStream = new LinkedBlockingQueue<>();
        players = new ArrayList<>();
        return new AwaitPlayers(this, playerCount, port);
    }
}
