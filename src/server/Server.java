package server;

import java.io.IOException;
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
    void Setup(String variant) // I changed it ~ Sara
    {
        if(variant.equals("Chaos")) game = new GameChaos(playerCount);
        else game = new GameClassic(playerCount);
    }

    private boolean isRunning;
    private GameState gameState;
    private BlockingQueue<String> serverInputStream;
    private List<Player> players;


    void Run() throws IOException
    {
        isRunning = true;
        gameState = LaunchNewGame();

        while(isRunning)
        {
            gameState.stateLoop();
        }

        System.out.println("Server Run Finished");
    }

    public GameState LaunchNewGame()
    {
        serverInputStream = new LinkedBlockingQueue<>();
        players = new ArrayList<>();
        return new AwaitPlayers(this, playerCount, port);
    }

    public void ChangeState(GameState newState)
    {
        gameState = newState;
    }

    public BlockingQueue<String> getQueue()
    {
        return serverInputStream;
    }

    public List<Player> getPlayerList()
    {
        return players;
    }

    public void setActive(boolean active)
    {
        isRunning = active;
    }
}
