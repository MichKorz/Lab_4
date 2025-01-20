package server;

public class BoardClassic extends Board{

    public BoardClassic(int playerCount)
    {
        super(playerCount);
    }

    @Override
    public void InitializeBoard(int playerCount)
    {
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                int tileOwner = boardOwners[i][j];
                if(playerCount == 3)
                {
                    if(tileOwner == 1 || tileOwner == 3 || tileOwner == 5)
                    {
                        board[i][j] = new Tile(tileOwner, tileOwner);
                    }
                    else board[i][j] = new Tile(tileOwner, 0);
                }
                else
                {
                    if(tileOwner > 0 && tileOwner <= playerCount)
                    {
                        board[i][j] = new Tile(tileOwner, tileOwner);
                    }
                    else board[i][j] = new Tile(tileOwner, 0);
                }

                if(tileOwner != 9) System.out.print(tileOwner + " ");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }
}
