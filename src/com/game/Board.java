package com.game;

public interface Board
{
    Boolean IsHomeOccupied(int player);
    Tile GetTileAt(int x, int y);
}
