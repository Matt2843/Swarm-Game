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

    @Override
    protected void handleConnection(Socket connection) throws IOException {

    }

    @Override
    public String getDescription() {
        return null;
    }
}
