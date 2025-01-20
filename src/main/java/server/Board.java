package server;

public abstract class Board
{
    protected Tile[][] board = new Tile[17][25];
    protected int[][] boardOwners = {
            {9,9,9,9,9,9,9,9,9,9,9,9,1,9,9,9,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,9,9,1,9,1,9,9,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,9,1,9,1,9,1,9,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,1,9,1,9,1,9,1,9,9,9,9,9,9,9,9,9},
            {6,9,6,9,6,9,6,9,0,9,0,9,0,9,0,9,0,9,3,9,3,9,3,9,3},
            {9,6,9,6,9,6,9,0,9,0,9,0,9,0,9,0,9,0,9,3,9,3,9,3,9},
            {9,9,6,9,6,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,3,9,3,9,9},
            {9,9,9,6,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,3,9,9,9},
            {9,9,9,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,9,9,9},
            {9,9,9,4,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,5,9,9,9},
            {9,9,4,9,4,9,0,9,0,9,0,9,0,9,0,9,0,9,0,9,5,9,5,9,9},
            {9,4,9,4,9,4,9,0,9,0,9,0,9,0,9,0,9,0,9,5,9,5,9,5,9},
            {4,9,4,9,4,9,4,9,0,9,0,9,0,9,0,9,0,9,5,9,5,9,5,9,5},
            {9,9,9,9,9,9,9,9,9,2,9,2,9,2,9,2,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,9,2,9,2,9,2,9,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,9,9,2,9,2,9,9,9,9,9,9,9,9,9,9,9},
            {9,9,9,9,9,9,9,9,9,9,9,9,2,9,9,9,9,9,9,9,9,9,9,9,9},
    };

    public Board(int playerCount) {
        InitializeBoard(playerCount);
    }

    public abstract void InitializeBoard(int playerCount);

    public Boolean IsHomeOccupied(int player) //player from 1-6
    {
        int destinationHome; //Destination home is directly on the other side of the board looking from player's home
        if(player%2 == 0) destinationHome = player-1;
        else destinationHome = player+1;

        int occupied = 0;
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                Tile tile = board[i][j];
                if(tile.getOwner() == destinationHome && tile.getPiece() == player) occupied++;
            }
        }

        return occupied == 10;
    }

    public Tile GetTileAt(int x, int y) {
        if(x < 0 || x >= 17 || y < 0 || y >= 25) return null;
        return board[x][y];
    }

    public void PrintBoard()
    {
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                int tileOwner = board[i][j].getOwner();
                int tilePiece = board[i][j].getPiece();
                if(tileOwner != 9) System.out.print(tilePiece + " ");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }

    public int HowManyPlayersWon(int howManyPlayersWon, int index) {
        int homeDestinationIndex; // which home does a player have to conquer to win (= index to finish game quickly)
        if(index%2 == 0) homeDestinationIndex = index-1;
        else homeDestinationIndex = index+1;

        int occupied = 0;
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                int tileOwner = board[i][j].getOwner();
                int tilePiece = board[i][j].getPiece();
                if(tileOwner == homeDestinationIndex && tilePiece == index)
                {
                    occupied++;
                    if(occupied == 10) break;
                }
            }
            if(occupied == 10) break;
        }

        if(occupied == 10) return howManyPlayersWon+1;
        else return howManyPlayersWon;
    }
}
