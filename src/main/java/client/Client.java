package client;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client
{
    ClientApplication app;
    Socket socket;
    BufferedReader reader;

    PrintWriter out;

    public Client(ClientApplication app)
    {
        this.app = app;
    }

    private boolean isConnecting = false;
    public void connect(String serverHost, int serverPort)
    {
        if (isConnecting)
        {
            System.out.println("Connection is already in progress.");
            return;
        }

        // Create a new thread to run the connection logic
        Thread connectionThread = new Thread(() -> {
            try
            {
                socket = new Socket(serverHost, serverPort);

                // Give server 5 seconds to send game variant
                socket.setSoTimeout(5000);

                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Try to read connection confirmation and game variant
                try
                {
                    String initialInfo = reader.readLine(); // Blocking call, will wait for input
                    // TODO Verify that the message is in fact a game variant
                    // Ensure the game launch happens on the JavaFX Application Thread
                    String[] params = initialInfo.split("_");
                    Platform.runLater(() -> app.launchGame(params[0], Integer.parseInt(params[1]), params[2]));
                    socket.setSoTimeout(0); // Turn off the timeout as the client will wait for server instructions for
                    // the remainder of the game
                }
                catch (SocketTimeoutException e)
                {
                    // Close the socket allowing the user to try again
                    // TODO send an error message to be displayed
                    System.out.println("Timeout: No message received within 5 seconds.");
                    socket.close();
                }
            }
            catch (IOException e)
            {
                // TODO send message that the socket creation failed
                throw new RuntimeException(e);
            }
            finally
            {
                // Once the connection process is done, reset the flag
                isConnecting = false;
            }
        });

        // Start the connection thread
        connectionThread.start();
    }

    public void LaunchClient(GUIController controller)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Listener listener = new Listener(reader, controller);
        executor.execute(listener);

        try
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            controller.client = this;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message)
    {
        out.println(message);
    }
}
