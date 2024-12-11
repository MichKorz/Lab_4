public class GameClassic implements Game
{
    Board board;
    Ruleset ruleset;

    public GameClassic()
    {
        board = new BoardClassic();
        ruleset = new RulesetClassic();
    }

    @Override
    public boolean ValidateMove(String move)
    {
        return true;
    }

    @Override
    public boolean IsOver()
    {
        return true;
    }
}
