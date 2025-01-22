package server;

import java.io.IOException;
import java.util.ArrayList;

public class Replay implements GameState
{
    private final Server server;
    private final ArrayList<String> ReplayMoves;

    public Replay(Server server, ArrayList<String> ReplayMoves)
    {
        this.server = server;
        this.ReplayMoves = ReplayMoves;
    }

    @Override
    public void stateLoop() throws IOException
    {
        server.sendChatMessage("/r_filler");
        server.Setup();

        server.Sleep(600);

        System.out.println(ReplayMoves);
        server.moves = new ArrayList<>();
        for(String move : ReplayMoves)
        {
            System.out.println(move);
            String[] split = move.split("_");
            if (server.game.ValidateMove(split[1]))
            {
                server.PropagateMove(split[1]);
                server.game.setIsTurnOver(true);
                server.game.setIsTurnOver(false);
            }
            server.Sleep(400);
        }
        endState();
    }

    @Override
    public void endState()
    {
        server.ChangeState(new GameLoop(server));
    }
}
