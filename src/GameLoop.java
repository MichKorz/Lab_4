import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class GameLoop implements GameState
{
    Server server;
    BlockingQueue<String> moveIn;

    public GameLoop(Server server)
    {
        this.server = server;
        moveIn = server.getQueue();
    }

    @Override
    public void stateLoop()
    {
        int playerCount = server.getPlayerList().size();
        Player currentPlayer;
        int index = new Random().nextInt(playerCount);
        while (true)
        {
            index = (index + 1)%playerCount;
            currentPlayer = server.getPlayerList().get(index);
            currentPlayer.setTurn(true);
            System.out.println("player's: " + index + " turn");

            String move = "";
            try
            {
                move = moveIn.take();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException();
            }

            if (server.game.ValidateMove(move)) PropagateMove(move);
            currentPlayer.setTurn(false);
            if (server.game.IsOver()) break;
        }
        endState();
    }

    @Override
    public void endState()
    {
        System.out.println("Game over");
        server.ChangeState(new EndGame(this.server));
    }

    void PropagateMove(String move)
    {
        for (Player player : server.getPlayerList())
        {
            player.out.println(move);
        }
    }
}
