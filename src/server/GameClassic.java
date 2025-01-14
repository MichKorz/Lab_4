package server;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameClassic extends Game
{
    public GameClassic(int playerCount)
    {
        super(playerCount);
        isTurnOver = new AtomicBoolean(false);
        howManyPlayersWon = 0;
        movesMade = 0;
    }

    @Override
    public void InitializeGame(int playerCount) {
        board = new BoardClassic(playerCount);
        ruleset = new RulesetClassic(board);
    }

}