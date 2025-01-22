package server;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        DatabaseApp database = new DatabaseApp();

        Server server = Server.getInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Integer.parseInt(args[2]), args[3], database);
        server.Setup();
        server.Run();
    }
}
