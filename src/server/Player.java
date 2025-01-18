package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Player implements Runnable
{
    private Server server;

    private final Socket socket;
    private PrintWriter out;
    final BlockingQueue<String> moveOut;

    String initialMessage;

    private boolean isTurn;

    public Player(Server server, Socket socket, BlockingQueue<String> moveStream, String initialMessage)
    {
        this.server = server;
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
            interpretMessage(line);
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
        if(out != null) // out is null if there's only one player
        {
            synchronized (out)
            {
                out.println(message);
            }
        }
    }

    void interpretMessage(String message)
    {
        String[] comps = message.split("_");
        switch (comps[0])
        {
            case "/c":
                server.sendChatMessage(comps[1]);
                break;

            case "/h":
                if (!getTurn()) break;
                String[] cords = comps[1].split(" ");
                String output = "/h_" + server.game.SetHighlightedTiles(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]));
                System.out.println("(Debug print) output sent: " + output);
                sendMessage(output);
                break;

            case "/m":
                if (getTurn()) moveOut.add(comps[1]);
                break;

            case "/e":
                if (getTurn())
                {
                    server.game.isTurnOver.set(true);
                    moveOut.add("E");
                }
                break;
        }
    }
}
