package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.server.database.EventBank;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matt on 03/16/2017.
 */
public class GameNode extends ServerNode {

    private EventBank eventBank;

    public GameNode() {
        eventBank = new EventBank();
        addNodeToMothership();
    }

    public EventBank getEventBank() {
        return eventBank;
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO game_nodes (id, user_count) VALUES ('" + getNodeId() + "'," + usersConnected + ")";
    }

    @Override
    public String getDescription() {
        return "Game Node";
    }

    @Override public String nextInPrimitiveChain() {
        return null;
    }
}
