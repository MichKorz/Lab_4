package server;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Server server = Server.getInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        server.Setup(args[2]);
        server.Run();
    }
}
