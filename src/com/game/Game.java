package com.game;

public interface Game
{
    void SetHighlightedTiles(int x, int y);
    boolean ValidateMove(String move);
    boolean isTurnOver();
    boolean IsOver();
}
