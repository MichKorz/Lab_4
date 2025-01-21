package server;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class GameLoop implements GameState
{
    int howManyPlayersWon;
    Server server;
    BlockingQueue<String> moveIn;
    int botCount;
    Bot bot;

    public GameLoop(Server server)
    {
        howManyPlayersWon = 0;
        this.server = server;
        botCount = server.getBotCount();
        moveIn = server.getQueue();
        if(botCount != 0) bot = new Bot(server.board, server);
    }

    @Override
    public void stateLoop()
    {
        int playerCount = server.getPlayerList().size();

        ArrayList<Integer> playerBoardIndexes = GetPlayerList(playerCount+botCount);
        Player currentPlayer;

        int index = new Random().nextInt(playerCount);
        int lastPlayerIndex = index; //index of the player that starts the game

        // to get player's index on board: playerBoardIndexes.get(index)
        while (true)
        {
            index = (index + 1)%playerCount;
            System.out.println("(INDEX): "+index);

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
                        System.out.println("(PLAYER) turn is over for: " + move);
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
            currentPlayer.setTurn(false);

            if (howManyPlayersWon < server.game.HowManyWonGame())
            {
                System.out.println("OMG! " + howManyPlayersWon + " won the game");
                server.sendChatMessage("/c_OMG! " + howManyPlayersWon + "st player won the game");
                howManyPlayersWon = server.game.HowManyWonGame();
                if(howManyPlayersWon == (playerCount-1))
                {
                    server.sendChatMessage(" /c_This is the end now ;((( But ooo, ooo" +
                            "Ooo, oo" +
                            "But a nuclear blast" +
                            "Or orange ball of gas" +
                            "Couldn't break me somehow" +
                            "Baby it's just the end now" +
                            "Cold winds blow" +
                            "Across this barren world of ash" +
                            "But its not my fault love is fleeting in a flash" +
                            "But oh, how the sky still burns for you");
                    break;
                }
            }

            Sleep(250);

            //bots play after the end of turn
            if(botCount != 0 && index == lastPlayerIndex)
            {
                for(int i = 1; i <= botCount; i++)
                {
                    int botIndex = getBotBoardIndex(i);
                    System.out.println("(BOT): "+botIndex);

                    String botMove = bot.moveBot(botIndex);

                    if(!server.game.ValidateMove(botMove))
                    {
                        System.out.println("isTurnOver = " + server.game.isTurnOver());
                        System.out.println("(ERROR): "+botMove);
                    }
                    else
                    {
                        PropagateMove(botMove);
                    }
                    server.game.setIsTurnOver(true);
                    server.game.setIsTurnOver(false);
                    server.board.PrintBoard();

                    Sleep(250);
                }
            }


        }
        endState();
    }

    void Sleep(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            throw new RuntimeException();
        }
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
        System.out.println("PROPAGATING "+message);
        for (Player player : server.getPlayerList())
        {
            player.sendMessage(message);
        }
    }

    private int getBotBoardIndex(int n)
    {
        int playerCount = server.getPlayerList().size();
        ArrayList<Integer> playerBoardIndexes = GetPlayerList(botCount + playerCount);
        return playerBoardIndexes.get(playerCount-1+n);
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
