package com.swarmer.server.nodes;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 03/16/2017.
 */
public class GameNode extends ServerNode {

    protected GameNode(int port) throws IOException {
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
