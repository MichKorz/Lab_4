package com.game;

import java.io.IOException;

public interface GameState
{
    void stateLoop() throws IOException;
    void endState();
}
