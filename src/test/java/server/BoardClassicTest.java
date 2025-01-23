package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardClassicTest
{

    @Test
    void howManyPlayersWon()
    {
        Board testBoard = new BoardClassic(2);

        for(int i = 0; i < 5; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                testBoard.board[i][j].setPiece(2);
            }
        }

        for(int i = 12; i < 17; i++)
        {
            for(int j = 0; j < 25; j++)
            {
                testBoard.board[i][j].setPiece(1);
            }
        }

        int result = 0;
        result = testBoard.HowManyPlayersWon(result, 1);
        assertEquals(1, result, "Expected: 1");

        result = testBoard.HowManyPlayersWon(result, 2);
        assertEquals(2, result, "Expected: 2");
    }
}