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
    private final int botCount;
    public String gameVariant;
    private final int port;

    private Server(int port, int playerCount, int botCount, String gameVariant)
    {
        this.port = port;
        this.playerCount = playerCount;
        this.botCount = botCount;
        this.gameVariant = gameVariant;

        if(playerCount == 0)
        {
            System.out.println("(FATAL ERROR) Player count is zero");
            System.out.println("Server is shutting down");
            System.exit(0);
        }
    }

    public static Server getInstance(int port, int playersCount, int botCount, String gameVariant)
    {
        if (instance == null)
        {
            instance = new Server(port, playersCount, botCount, gameVariant);
        }
        return instance;
    }

    public Game game;
    public Board board;
    public Ruleset ruleset;

    // Game variant setup
    void Setup() // I changed it ~ Sara
    {
        if(gameVariant.equals("Chaos"))
        {
            board = new BoardChaos(playerCount+botCount);
            ruleset = new RulesetClassic(board);
        }
        else
        {
            board = new BoardClassic(playerCount+botCount);
            ruleset = new RulesetClassic(board);
        }
        game = new Game(board, ruleset);
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

    public int getBotCount() { return botCount; }

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
