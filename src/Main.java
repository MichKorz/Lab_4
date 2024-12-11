public class Main
{
    public static void main(String[] args)
    {
        Server server = Server.getInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        server.Setup();
        server.Run();
    }
}
