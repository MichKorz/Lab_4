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
        String[] comps = message.split("_");

        switch (comps[0])
        {
            case "/m":
                String[] params = comps[1].split(" ");
                // Bad way to do it, but it works
                controller.movePiece(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                break;

            case "/h":
                if (comps.length > 1) controller.highlightCircles(comps[1]);
                break;

            case "/c":
                if (comps.length > 1) controller.printChatMessage(comps[1]);
                break;

            case "/y":
                if (comps.length > 1)
                {
                    controller.setYourTurnField(Integer.parseInt(comps[1]) == 1);
                }
                break;
        }
    }

}
