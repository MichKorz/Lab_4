package com.game;

import java.io.IOException;

public class EndGame implements GameState
{
    Server server;

    public EndGame(Server server)
    {
        this.server = server;
    }

    @Override
    public void stateLoop() throws IOException
    {
        for (Player player : server.getPlayerList())
        {
            player.getSocket().close();
        }
        server.getPlayerList().clear();
        server.setActive(false);
        endState();
    }

    @Override
    public void endState()
    {
        System.out.println("Game shut down");
        server.ChangeState(null);
    }
}
