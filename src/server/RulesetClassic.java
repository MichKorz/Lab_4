package server;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;

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
    public String GetHighlightTiles(int x, int y) // get all possible moves for tile at position (x,y)
    {
        Tile tile = board.GetTileAt(x, y);
        if(tile == null || tile.getPiece() == 0) return "";
        possibleTiles.clear();

        CheckForTiles(x, y, -1, 1);
        CheckForTiles(x, y, 1, 1);
        CheckForTiles(x, y, 1, -1);
        CheckForTiles(x, y, -1, -1);

        StringBuilder highlight = new StringBuilder();
        for(Point p : possibleTiles)
        {
            highlight.append(p.x).append(" ").append(p.y).append(" ");
        }
        return highlight.toString();

        /*System.out.println("Highlighted tiles for tile: " + x + ", " + y);
        for(Point p : possibleTiles)
        {
            System.out.println("H X: " + p.x + ", Y: " + p.y);
        }*/
    }

    private void CheckForTiles(int x, int y, int xOffset, int yOffset)
    {
        Tile newTile = board.GetTileAt(x+xOffset, y+yOffset);
        System.out.print("Checking for tile: " + x + ", " + y + ", " + xOffset + ", " + yOffset);
        System.out.println(" newtile piece: " + newTile.getPiece() + ", owner: " + newTile.getOwner());
        if(newTile != null && newTile.getOwner() != 9) // check if tile belongs to the board
        {
            if(newTile.getPiece() == 0)
            {
                if(point == null)
                {
                    possibleTiles.add(new Point(x+xOffset, y+yOffset));
                    System.out.print("Tile no hop. ");
                }
            }
            else
            {
                newTile = board.GetTileAt(x+xOffset+xOffset, y+yOffset+yOffset);
                if(newTile != null && newTile.getPiece() == 0 && newTile.getOwner() != 9)
                {
                    System.out.print("Tile hop. ");
                    possibleTiles.add(new Point(x+xOffset+xOffset, y+yOffset+yOffset));
                }
            }
        }
    }

    @Override
    public Boolean validateMove(int[] commands, AtomicBoolean isTurnOver) // initialX initialY destinationX destinationY validato playerIndex
    {
        if(point != null) {
            System.out.println("Line 64");
            if(point.x != commands[0] && point.y != commands[1]) return false;
        }

        final int playerIndex = commands[commands.length-1];
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
            if(possibleTile.x == newPos.x && possibleTile.y == newPos.y)
            {
                if((abs(possibleTile.x-x) + abs(possibleTile.y-y)) == 2){ // if piece moves without hoping over
                    isTurnOver.set(true);
                    point = null;
                }
                else{ // if piece hops
                    point = newPos;
                    String w = GetHighlightTiles(finalX, finalY); // MOVE IT TO GAMELOOP !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if(possibleTiles.isEmpty())
                    {
                        point = null;
                        isTurnOver.set(true);
                    }
                }
                System.out.println("Possible tile: " + possibleTile.getX() + " " + possibleTile.getY());
                initialTile.setPiece(0);
                Tile destinationTile = board.GetTileAt(finalX, finalY);
                destinationTile.setPiece(playerIndex);
                return true;
            }
        }

        return false;
    }
}
