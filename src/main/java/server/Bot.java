package server;

import java.awt.*;
import java.util.ArrayList;

public class Bot {
    Board board;
    Server server;

    ArrayList<Point> movableTiles;
    Point destination;
    Point fromTile;
    Point toTile;
    int index;

    public Bot(Board board, Server server)
    {
        this.board = board;
        this.server = server;
    }

    public String moveBot(int index)
    {
        fromTile = null;
        toTile = null;
        this.index = index;
        movableTiles = new ArrayList<>();

        // get all tiles that have possible moves
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++)
            {
                Tile tile = board.GetTileAt(i, j);
                if (tile.getPiece() == index) // if tile has a piece belonging to the bot
                {
                    if (!server.game.SetHighlightedTiles(i, j).isEmpty())
                    {
                        movableTiles.add(new Point(i, j));
                        System.out.print("x: " + i + " y: " + j + " | ");
                    }
                }
            }
        }
        System.out.println();

        getMove(); // sets values of fromTile and toTile

        if(fromTile == null || toTile == null) return "";
        String move = fromTile.x + " " + fromTile.y + " " + toTile.x + " " + toTile.y + " " + index;
        System.out.println("BOT'S MOVE IS: " + move);
        return move;
    }

    private void getMove()
    {
        destination = getDestinationTile(); // get the end tile of the destination home
        Point[] bestPieceMoves = new Point[movableTiles.size()];
        double bestDistance = 0;

        for (int i = 0; i < movableTiles.size(); i++)
        {
            bestPieceMoves[i] = getBestPieceMove(movableTiles.get(i)); // for every movable tile get its best toMove
            if(bestPieceMoves[i] == null) continue;
            double tempDistance = getDistance(movableTiles.get(i), bestPieceMoves[i]);
            if(tempDistance > bestDistance)
            {
                bestDistance = tempDistance;
                fromTile = movableTiles.get(i);
                toTile = bestPieceMoves[i];
            }
        }
    }

    private Point getDestinationTile()
    {
        if (index == 1) return new Point(16, 12);
        if (index == 2) return new Point(0, 12);
        if (index == 3) return new Point(12, 0);
        if (index == 4) return new Point(4, 24);
        if (index == 5) return new Point(4, 0);
        else return new Point(12, 24);
    }

    private Point getBestPieceMove(Point from)
    {
        String possibleMoves = server.game.SetHighlightedTiles(from.x, from.y);
        String[] splitMoves = possibleMoves.split(" ");
        int[] movesCoords = new int[splitMoves.length];

        for (int i = 0; i < splitMoves.length; i++)
        {
            movesCoords[i] = Integer.parseInt(splitMoves[i]);
        }

        double fromDistance = getDistance(from, destination); // distance from fromTile to destinationTile

        Point bestMove = new Point(movesCoords[0], movesCoords[1]); // there is at least one move in SetHighlightedTiles
        double bestDistance = getDistance(bestMove, destination);
        if(bestDistance > fromDistance) // if piece moves in opposite direction to destination, set move to null
        {
            bestMove = null;
            bestDistance = fromDistance;
        }

        for (int i = 2; i < splitMoves.length; i += 2)
        {
            Point tile = new Point(movesCoords[i], movesCoords[i + 1]);
            double tempDistance = getDistance(tile, destination);
            if (tempDistance < bestDistance)
            {
                bestMove = tile;
                bestDistance = tempDistance;
            }
        }

        return bestMove;
    }

    private double getDistance(Point from, Point to)
    {
        return Math.sqrt(Math.pow(Math.abs(from.x - to.x),2) + Math.pow(Math.abs(from.y - to.y),2));
    }
}
