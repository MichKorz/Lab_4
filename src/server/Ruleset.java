package server;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Ruleset
{
    void HighlightTiles(int x, int y);
    Boolean validateMove(int[] commands, AtomicBoolean isTurnOver);
}
