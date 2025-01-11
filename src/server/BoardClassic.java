package server;

public class BoardClassic implements Board
{
    Tile[][] board = new Tile[17][25];
    int[][] boardOwners = {
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

    public BoardClassic(int playerCount)
    {
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                int tileOwner = boardOwners[i][j];
                if(tileOwner > 0 && tileOwner <= playerCount)
                {
                    board[i][j] = new Tile(tileOwner, tileOwner);
                }
                else board[i][j] = new Tile(tileOwner, 0);

                //Dupaprint
                if(tileOwner != 9) System.out.print(tileOwner + " ");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }

    @Override
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

    @Override
    public Tile GetTileAt(int x, int y) {
        if(x < 0 || x >= 17 || y < 0 || y >= 25) return null;
        return board[x][y];
    }
}
