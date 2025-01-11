public class GameClassic implements Game
{
    Board board;
    Ruleset ruleset;

    int movesMade; // Temp for debug purposes

    public GameClassic(int playerCount)
    {
        board = new BoardClassic(playerCount);
        ruleset = new RulesetClassic();

        movesMade = 0;
    }

    @Override
    public boolean ValidateMove(String move)
    {
        movesMade++;
        return true;
    }

    @Override
    public boolean IsOver()
    {
        return movesMade > 5;
    }
}
