package com.game;

import java.awt.*;
import java.util.ArrayList;

public class RulesetClassic implements Ruleset
{
    private final Board board;
    private final ArrayList<Point> possibleTiles;
    private Point point = null;

    public RulesetClassic(Board board)
    {
        this.board = board;
        possibleTiles = new ArrayList<>();
    }

    @Override
    public void HighlightTiles(int x, int y) // get all possible moves for tile at position (x,y)
    {
        Tile tile = board.GetTileAt(x, y);
        if(tile == null) return;
        possibleTiles.clear();

        CheckForTiles(x, y, -1, 1);
        CheckForTiles(x, y, 1, 1);
        CheckForTiles(x, y, 1, -1);
        CheckForTiles(x, y, -1, -1);
    }

    private void CheckForTiles(int x, int y, int xOffset, int yOffset)
    {
        Tile newTile = board.GetTileAt(x+xOffset, y+yOffset);
        if(newTile != null && newTile.getOwner() != 9) // check if tile belongs to the board
        {
            if(newTile.getPiece() == 0) possibleTiles.add(new Point(x+xOffset, y+yOffset));
            else
            {
                newTile = board.GetTileAt(x+xOffset+xOffset, y+yOffset+yOffset);
                if(newTile.getPiece() == 0) possibleTiles.add(new Point(x+xOffset, y+yOffset));
            }
        }
    }

    @Override
    public Boolean validateMove(int[] commands, boolean isTurnOver)
    {
        final int playerIndex = commands[4];
        final int x = commands[0];
        final int y = commands[1];
        final int finalX = commands[2];
        final int finalY = commands[3];

        Tile initialTile = board.GetTileAt(x, y);
        if(initialTile == null) return false;
        if(initialTile.getPiece() != playerIndex) return false;

        Point newPos = new Point(finalX, finalY);
        for(Point possibleTile : possibleTiles)
        {
            if(possibleTile == newPos)
            {
                initialTile.setPiece(0);
                Tile destinationTile = board.GetTileAt(finalX, finalY);
                destinationTile.setPiece(playerIndex);
                HighlightTiles(finalX, finalY);
            }
        }
        return false;
    }
}
