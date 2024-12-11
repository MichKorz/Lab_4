import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class GameLoop implements GameState
{
    Server server;
    BlockingQueue<String> moveIn;

    public GameLoop(Server server)
    {
        this.server = server;
        moveIn = server.moveInputStream;
    }

    @Override
    public void stateLoop()
    {
        Player currentPlayer;
        int index = new Random().nextInt(server.players.size());
        while (true)
        {
            index = (index + 1)%server.players.size();
            currentPlayer = server.players.get(index);
            currentPlayer.setTurn(true);

            String move = "";
            try
            {
                move = moveIn.take();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (server.game.ValidateMove(move)) PropagateMove(move);
            currentPlayer.setTurn(false);
            if (server.game.IsOver()) break;
        }
        System.out.println("Game over");
        endState();
    }

    @Override
    public void endState()
    {
        server.gameState = new EndGame();
    }

    void PropagateMove(String move)
    {
        for (Player player : server.players)
        {
            player.out.println(move);
        }
    }
}
