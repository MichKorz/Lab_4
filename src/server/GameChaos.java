package server;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameChaos extends Game
{
    public GameChaos(int playerCount)
    {
        super(playerCount);
        isTurnOver = new AtomicBoolean(false);
        howManyPlayersWon = 0;
        movesMade = 0;
    }

    @Override
    public void InitializeGame(int playerCount) {
        board = new BoardChaos(playerCount);
        ruleset = new RulesetClassic(board);
    }

}
