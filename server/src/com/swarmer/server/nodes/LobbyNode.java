package com.swarmer.server.nodes;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyNode extends ServerNode {

    @Override public String generateInsertQuery() {
        return "INSERT INTO lobby_nodes (id) VALUES ('" + getNodeId() + "')";
    }

    @Override
    public String getDescription() {
        return "Lobby Node";
    }
}
