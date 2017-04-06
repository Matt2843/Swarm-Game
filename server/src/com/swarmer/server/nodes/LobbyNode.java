package com.swarmer.server.nodes;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyNode extends ServerNode {

    protected LobbyNode(int port) throws IOException {
        super(port);
    }

    @Override public String generateInsertQuery() {
        return null;
    }

    @Override public String getDescription() {
        return null;
    }

    @Override protected void handleConnection(Socket connection) throws IOException {

    }
}
