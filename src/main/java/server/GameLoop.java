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

    private boolean saved;
    private boolean load;

    public GameLoop(Server server)
    {
        howManyPlayersWon = 0;
        this.server = server;
        botCount = server.getBotCount();
        moveIn = server.getQueue();
        if(botCount != 0) bot = new Bot(server.board, server);

        saved = false;
        load = false;
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
                    if (move.startsWith("save"))
                    {
                        System.out.println("Initializing saving");
                        String[] comps = move.split(" ");
                        //Checks if correct length
                        saveGame(comps[1]);
                        saved = true;
                        break;
                    }

                    if (move.startsWith("load"))
                    {
                        System.out.println("Initializing Loading");
                        String[] comps = move.split(" ");
                        //Checks if correct length
                        loadGame(comps[1]);
                        break;
                    }

                    move = move.concat(" ").concat(Integer.toString(playerBoardIndexes.get(index)));
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException();
                }

                if (server.game.ValidateMove(move))
                {
                    System.out.println("Successfully moved " + move);
                    server.PropagateMove(move); //update board then propagete it
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

            if( saved ) break;

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

            server.Sleep(250);

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
                        server.PropagateMove(botMove);
                    }
                    server.game.setIsTurnOver(true);
                    server.game.setIsTurnOver(false);
                    server.board.PrintBoard();

                    server.Sleep(250);
                }
            }


        }
        if(!load) endState();
    }

    @Override
    public void endState()
    {
        System.out.println("Game over");
        server.ChangeState(new EndGame(this.server));
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

    private void saveGame(String name)
    {
        DatabaseApp database = server.database;

        int newId = database.getHighestId() + 1;
        database.insertGame(name);

        for (String move : server.moves)
        {
            database.insertMove(newId, move);
        }
    }

    private void loadGame(String name)
    {
        System.out.println("Loading game " + name);
        DatabaseApp database = server.database;
        int game_id = database.findGameId(name);

        System.out.println(game_id);

        if (game_id == -1) server.sendChatMessage("Game not found");
        else
        {
            saved = true;
            load = true;
            server.ChangeState(new Replay(server, database.readMoves(game_id)));
        }
    }
}
