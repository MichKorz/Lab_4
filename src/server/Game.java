package server;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Game
{
    Board board;
    Ruleset ruleset;
    int howManyPlayersWon;
    AtomicBoolean isTurnOver;
    int movesMade;

    public Game(int playerCount)
    {
        InitializeGame(playerCount);
        isTurnOver = new AtomicBoolean(false);
        howManyPlayersWon = 0;
        movesMade = 0;
    }

    public abstract void InitializeGame(int playerCount);

    public void SetHighlightedTiles(int x, int y) { // For a tile at x, y set a list of all possible moves
        ruleset.HighlightTiles(x, y);
    }

    public boolean ValidateMove(String move) // initialX initialY destinationX destinationY validato playerIndex
    {

        String[] splitMove = move.split(" ");
        int[] commands = new int[splitMove.length];

        for (int i = 0; i < splitMove.length; i++) {
            commands[i] = Integer.parseInt(splitMove[i]);
        }

        System.out.println("X = " + commands[0] + " Y = " + commands[1]);
        SetHighlightedTiles(commands[0], commands[1]); // Not supposed to be here!!!!!!!!!

        Boolean validation = ruleset.validateMove(commands, isTurnOver);
        if(validation) {
            movesMade++;
            board.PrintBoard(); //DOEFWEPFdddddddddddddddddddddddd
            howManyPlayersWon = board.HowManyPlayersWon(howManyPlayersWon, commands[4]);
            return true;
        }
        return false;

    }

    public boolean isTurnOver() { // if player had moved (not hoped) then its over. if player hoped but then there are no more hops, its over
        return isTurnOver.get();
    }

    public int HowManyWonGame()
    {
        return howManyPlayersWon;
    }
}
