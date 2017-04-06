package com.swarmer.server.nodes;

import java.io.IOException;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyNode extends ServerNode {

    protected LobbyNode(int port) throws IOException {
        super(port);
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO lobby_nodes (id, user_count) VALUES ('" + getNodeUUID() + "'," + usersConnected + ")";
    }

    @Override
    public String getDescription() {
        return "Lobby Node";
    }

    @Override protected void awaitConnection() {

    }

    @Override public String nextInPrimitiveChain() {
        return null;
    }
}
