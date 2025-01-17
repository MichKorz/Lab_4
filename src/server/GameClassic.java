package server;

public class GameClassic extends Game
{
    public GameClassic(int playerCount, Board board)
    {
        super(playerCount, board);
    }

    @Override
    public void InitializeGame(int playerCount, Board board) {
        ruleset = new RulesetClassic(board);
    }

}
