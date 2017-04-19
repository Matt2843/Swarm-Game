package com.swarmer.server.units;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 03/16/2017.
 */
public class GameUnit extends ServerUnit {

    protected GameUnit(int port) {
        super(port);
    }

    @Override
    protected void handleConnection(Socket connection) throws IOException {

    }

    @Override
    public String getDescription() {
        return "game_units";
    }

    public static void main(String[] args) {
        new GameUnit(ServerUnit.GAME_UNIT_TCP_PORT);
    }
}
