package com.game;

public interface Ruleset
{
    void HighlightTiles(int x, int y);
    Boolean validateMove(int[] commands, boolean isTurnOver);
}
