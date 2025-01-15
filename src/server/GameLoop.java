package server;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class GameLoop implements GameState
{
    int howManyPlayersWon;
    Server server;
    BlockingQueue<String> moveIn;

    public GameLoop(Server server)
    {
        howManyPlayersWon = 0;
        this.server = server;
        moveIn = server.getQueue();
    }

    @Override
    public void stateLoop()
    {
        int playerCount = server.getPlayerList().size();
        ArrayList<Integer> playerBoardIndexes = GetPlayerList(playerCount);
        Player currentPlayer;
        int index = new Random().nextInt(playerCount);
        // to get player's index on board: playerBoardIndexes.get(index)
        while (true)
        {
            index = (index + 1)%playerCount;
            System.out.println("player's: " + playerBoardIndexes.get(index) + " turn");

            currentPlayer = server.getPlayerList().get(index);
            currentPlayer.setTurn(true);
            currentPlayer.sendMessage("/y_1");

            while(true)
            {
                String move = "";
                try
                {
                    move = moveIn.take();
                    move = move.concat(" ").concat(Integer.toString(playerBoardIndexes.get(index)));
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException();
                }

                if (server.game.ValidateMove(move))
                {
                    System.out.println("Successfully moved " + move);
                    PropagateMove(move); //update board then propagete it
                    if(server.game.isTurnOver())
                    {
                        server.game.isTurnOver.set(false);
                        break;
                    }
                }
                else
                {
                    System.out.println("Wrong move" + move);
                    if(server.game.isTurnOver())
                    {
                        server.game.setIsTurnOver(false);
                        break;
                    }
                }
            }

            currentPlayer.sendMessage("/y_0");
            server.sendChatMessage(" /c_This is the end now ;((( But ooo, ooo");
            currentPlayer.setTurn(false);
            if (howManyPlayersWon < server.game.HowManyWonGame())
            {
                System.out.println("OMG! " + howManyPlayersWon + " won the game");
                server.sendChatMessage("/c_OMG! " + howManyPlayersWon + "st player won the game");
                howManyPlayersWon = server.game.HowManyWonGame();
                if(howManyPlayersWon == (playerCount-1))
                {
                    server.sendChatMessage(" /c_This is the end now ;((( But ooo, ooo\n" +
                            "Ooo, oo\n" +
                            "But a nuclear blast\n" +
                            "Or orange ball of gas\n" +
                            "Couldn't break me somehow\n" +
                            "Baby it's just the end now\n" +
                            "Cold winds blow\n" +
                            "Across this barren world of ash\n" +
                            "But its not my fault love is fleeting in a flash\n" +
                            "But oh, how the sky still burns for you");
                    break;
                }
            }
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
        String message = "/m_" + move;
        for (Player player : server.getPlayerList())
        {
            player.sendMessage(message);
        }
    }

    ArrayList<Integer> GetPlayerList(int playerCount) {
        ArrayList<Integer> order = new ArrayList<>();

        if(playerCount != 3)
        {
            for (int i = 1; i <= playerCount; i += 2) order.add(i);
            for (int i = 2; i <= playerCount; i += 2) order.add(i);
        }
        else
        {
            order.add(1);
            order.add(3);
            order.add(5);
        }

        return order;
    }
}
