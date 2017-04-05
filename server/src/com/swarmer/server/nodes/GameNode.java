package com.swarmer.server.nodes;

import com.swarmer.server.database.EventBank;

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
