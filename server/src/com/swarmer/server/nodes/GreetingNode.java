package com.swarmer.server.nodes;

import com.swarmer.server.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Matt on 03/16/2017.
 */
public class GreetingNode extends ServerNode {

    private ServerSocket server;
    private Socket connection;
    private final int port;

    private boolean running = true;

    public GreetingNode(int port) {
        this.port = port;
    }

    @Override public void run() {
        try {
            server = new ServerSocket(port);
            waitForConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        while(running) {
            connection = server.accept();
            Connection newCon = new Connection(connection, this);
            newCon.start();
        }
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO greeting_nodes (id) VALUES ('" + getNodeId() + "')";
    }

    @Override
    public String getDescription() {
        return "GreetingNode";
    }

    // TODO: First client server contact, Establish connection


}
