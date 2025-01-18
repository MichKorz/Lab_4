package server;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Server server = Server.getInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
        server.Setup();
        server.Run();
    }
}
