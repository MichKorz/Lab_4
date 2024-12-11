public class GameClassic implements Game
{
    Board board;
    Ruleset ruleset;

    public GameClassic()
    {
        board = new BoardClassic();
        ruleset = new RulesetClassic();
    }
}
