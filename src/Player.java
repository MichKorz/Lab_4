import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class Player implements Runnable
{
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private PrintWriter moveOut;

    private boolean isTurn;

    public Player(Socket socket, PipedOutputStream moveStream) throws IOException
    {
        this.socket = socket;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);

        this. moveOut = new PrintWriter(moveStream, true);
    }

    @Override
    public void run()
    {
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
            if (getTurn())
            {
                moveOut.println(line);
            }
        }
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
