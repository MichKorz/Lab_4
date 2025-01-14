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
}
