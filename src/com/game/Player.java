package com.game;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Player implements Runnable
{
    private final Socket socket;
    public PrintWriter out;
    BlockingQueue<String> moveOut;

    private boolean isTurn;

    public Player(Socket socket, BlockingQueue<String> moveStream)
    {
        this.socket = socket;
        moveOut = moveStream;
        isTurn = false;
    }

    @Override
    public void run()
    {
        Scanner in;
        try
        {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        while (in.hasNextLine())
        {
            var line = in.nextLine();
            System.out.println("(Debug print) received line: " + line);
            if (getTurn())
            {
                try
                {
                    moveOut.put(line);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Closing connection");
    }

    synchronized boolean getTurn()
    {
        return isTurn;
    }

    synchronized void setTurn(boolean turn)
    {
        this.isTurn = turn;
    }

    public Socket getSocket()
    {
        return socket;
    }
}
