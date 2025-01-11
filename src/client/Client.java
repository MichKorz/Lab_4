package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverHost = "localhost"; // Replace with the server's IP if needed
        int serverPort = 59001;          // Replace with the server's port

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in))
        {

            System.out.println("Connected to the server. Start typing messages:");

            new Thread(() -> {
                try (Scanner serverReader = new Scanner(socket.getInputStream()))
                {
                    String response;
                    while (serverReader.hasNextLine())
                    {
                        response = serverReader.nextLine();
                        System.out.println("Server: " + response);
                    }
                    System.out.println("Server stopped.");
                }
                catch (IOException e)
                {
                    System.out.println("Connection closed by server.");
                }
            }).start();

            // Main thread to handle user input and send messages to the server
            while (true)
            {
                System.out.print("You: ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input))
                {
                    System.out.println("Closing connection...");
                    break;
                }

                writer.println(input); // Send input to the server
            }

        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }
}
