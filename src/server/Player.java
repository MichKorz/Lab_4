package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Player implements Runnable
{
    private final Socket socket;
    private PrintWriter out;
    final BlockingQueue<String> moveOut;

    String initialMessage;

    private boolean isTurn;

    public Player(Socket socket, BlockingQueue<String> moveStream, String initialMessage)
    {
        this.socket = socket;
        moveOut = moveStream;
        isTurn = false;

        this.initialMessage = initialMessage;
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

        out.println(initialMessage);

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

    public void sendMessage(String message)
    {
        synchronized (moveOut)
        {
            out.println(message);
        }
    }
}
