import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class Player implements Runnable
{
    private final Socket socket;
    public PrintWriter out;
    private final PrintWriter moveOut;

    private boolean isTurn;

    public Player(Socket socket, PipedOutputStream moveStream)
    {
        this.socket = socket;
        this.moveOut = new PrintWriter(moveStream, true);
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
                moveOut.println(line);
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
}
