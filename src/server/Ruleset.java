package server;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Ruleset
{
    String GetHighlightTiles(int x, int y);
    Boolean validateMove(int[] commands, AtomicBoolean isTurnOver);
}
