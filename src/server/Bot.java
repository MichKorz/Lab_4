package server;

import java.awt.*;
import java.util.ArrayList;

public class Bot {
    Board board;
    Server server;

    ArrayList<Point> movableTiles;
    Point fromTile;
    Point toTile;

    public Bot(Board board, Server server)
    {
        this.board = board;
        this.server = server;
    }

    public String moveBot(int index)
    {
        movableTiles = new ArrayList<>();

        // get all tiles that have possible moves
        for(int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++) {
                Tile tile = board.GetTileAt(i,j);
                if(tile.getPiece() == index) // if tile has a piece belonging to the bot
                {
                    if(!server.game.SetHighlightedTiles(i,j).isEmpty())
                    {
                        movableTiles.add(new Point(i,j));
                        System.out.print("x: "+i+" y: "+j+" | ");
                    }
                }
            }
        }
        System.out.println();

        getBestMove();

        return fromTile.x + " " + fromTile.y + " " + toTile.x + " " + toTile.y;
    }

    private void getBestMove()
    {
        fromTile = movableTiles.get(0);
        String moves = server.game.SetHighlightedTiles(fromTile.x,fromTile.y);
        String[] splitBoard = moves.split(" ");
        toTile = new Point(Integer.parseInt(splitBoard[0]),Integer.parseInt(splitBoard[1]));
    }
}
