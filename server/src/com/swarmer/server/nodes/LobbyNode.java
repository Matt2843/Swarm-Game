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

    @Override
    protected void handleConnection(Socket connection) throws IOException {

    }

    @Override
    public String getDescription() {
        return null;
    }
}
