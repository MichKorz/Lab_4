package server;

public class GameClassic implements Game
{
    Board board;
    Ruleset ruleset;
    Boolean isTurnOver;

    int movesMade; // Temp for debug purposes

    public GameClassic(int playerCount)
    {
        board = new BoardClassic(playerCount);
        ruleset = new RulesetClassic(board);
        isTurnOver = false;

        movesMade = 0;
    }

    @Override
    public void SetHighlightedTiles(int x, int y) { // For a tile at x, y set a list of all possible moves
        ruleset.HighlightTiles(x, y);
    }

    @Override
    public boolean ValidateMove(String move) // initialX initialY destinationX destinationY playerIndex 4 validato
    {
        String[] splitMove = move.split(" ");
        int[] commands = new int[splitMove.length];

        for (int i = 0; i < splitMove.length; i++)
        {
            try{
                commands[i] = Integer.parseInt(splitMove[i]);
            }
            catch(NumberFormatException e) {
                return false;
            }
        }

        Boolean validation = ruleset.validateMove(commands, isTurnOver); //change it to a reference to be able to change its value in ruleset
        if(validation)
        {
            movesMade++;
            return true;
        }
        return false;

    }

    @Override
    public boolean isTurnOver() { // if player had moved (not hoped) then its over. if player hoped but then there are no more hops, its over
        return isTurnOver;
    }

    @Override
    public boolean IsOver()
    {
        return movesMade > 5;
    }
}
