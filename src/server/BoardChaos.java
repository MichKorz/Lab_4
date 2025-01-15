package server;

import java.util.ArrayList;
import java.util.Random;

public class BoardChaos extends Board
{
    public BoardChaos(int playerCount)
    {
        super(playerCount);
    }

    @Override
    public void InitializeBoard(int playerCount)
    {
        ArrayList<int[]> tilesOwnedByZero = new ArrayList<>();

        // Get all tiles owned by zero ( empty tile belonging to the center of the board)
        for (int i = 0; i < boardOwners.length; i++) {
            for (int j = 0; j < boardOwners[i].length; j++)
            {
                if (boardOwners[i][j] == 0) {
                    tilesOwnedByZero.add(new int[]{i, j});
                }
            }
        }

        // Set pieces randomly on tiles with no other pieces in the diamond center of the board
        Random random = new Random(1);
        for(int index = 1; index <= playerCount; index++)
        {
            for(int j = 0; j < 10; j++)
            {
                int[] coordinates = tilesOwnedByZero.get(random.nextInt(tilesOwnedByZero.size()));
                board[coordinates[0]][coordinates[1]] = new Tile(0, index);
                tilesOwnedByZero.remove(coordinates);
            }
        }

        // Fill the rest of the board with empty pieces
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                int tileOwner = boardOwners[i][j];
                if(GetTileAt(i,j) == null)
                {
                    board[i][j] = new Tile(tileOwner, 0);
                }


            }
        }

        PrintBoard();

    }
}
