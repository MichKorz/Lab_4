package server;

public class GameChaos extends Game
{
    public GameChaos(int playerCount, Board board)
    {
        super(playerCount, board);
    }

    @Override
    public void InitializeGame(int playerCount, Board board) {
        ruleset = new RulesetClassic(board);
    }

}
