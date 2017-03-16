package com.swarmer.server.utility;

import com.swarmer.server.Connection;
import com.swarmer.server.database.GameDatabase;
import com.swarmer.shared.communication.Message;

import java.io.IOException;

/**
 * Created by Matt on 02/27/2017.
 */
public class GameEvent {

    public final String GAME_ID;
    public static GameDatabase gameDatabase = new GameDatabase();


    public GameEvent(String GAME_ID) {
        this.GAME_ID = GAME_ID;
    }

    // TODO: Implement correct broadCasData method, should update all clients with player data and ant positions.
    public void broadCastData() throws IOException {
        Message m = new Message(gameDatabase.positionData);
        for(Connection c : gameDatabase.connectedClients) {
            c.sendMessage(m);
        }
    }



}
