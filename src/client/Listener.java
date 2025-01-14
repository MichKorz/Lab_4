package client;

import java.io.BufferedReader;
import java.io.IOException;

public class Listener implements Runnable
{
    BufferedReader reader;
    GUIController controller;

    public Listener(BufferedReader reader, GUIController controller)
    {
        this.reader = reader;
        this.controller = controller;
    }

    @Override
    public void run()
    {
        String message;
        try
        {
            // Read until the socket is closed
            while ((message = reader.readLine()) != null)
            {
                interpretMessage(message);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Socket closed. Closing Listener.");
    }

    void interpretMessage(String message)
    {
        System.out.println("Received: " + message);
    }

}
