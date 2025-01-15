package server;

import java.util.Objects;
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

    public String SetHighlightedTiles(int x, int y) { // For a tile at x, y set a list of all possible moves
        return ruleset.GetHighlightTiles(x, y);
    }

    public boolean ValidateMove(String move) // initialX initialY destinationX destinationY validato playerIndex
    {
        String[] splitMove = move.split(" ");
        if (splitMove[0].equals("E"))
            return false;

        int[] commands = new int[splitMove.length];
        for (int i = 0; i < splitMove.length; i++) {
            commands[i] = Integer.parseInt(splitMove[i]);
        }

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

    public void setIsTurnOver(boolean isTurnOver)
    {
        this.isTurnOver.set(isTurnOver);
        if(!isTurnOver) {
            ((RulesetClassic)ruleset).point = null;
        }
    }

    public int HowManyWonGame()
    {
        return howManyPlayersWon;
    }
}
