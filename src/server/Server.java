package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server
{
    private static Server instance;
    public int playerCount;
    public String gameVariant;
    private final int port;

    private Server(int port, int playerCount, String gameVariant)
    {
        this.port = port;
        this.playerCount = playerCount;
        this.gameVariant = gameVariant;
    }

    public static Server getInstance(int port, int playersCount, String gameVariant)
    {
        if (instance == null)
        {
            instance = new Server(port, playersCount, gameVariant);
        }
        return instance;
    }

    public Game game;
    public Board board;

    // Game variant setup
    void Setup() // I changed it ~ Sara
    {
        if(gameVariant.equals("Chaos"))
        {
            board = new BoardChaos(playerCount);
            game = new GameChaos(playerCount, board);
        }
        else
        {
            board = new BoardClassic(playerCount);
            game = new GameClassic(playerCount, board);
        }
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


    public void sendChatMessage(String message)
    {
        for (Player player : getPlayerList())
        {
            System.out.println("(Debug print) output sent: " + message);
            player.sendMessage(message);
        }
    }
}
