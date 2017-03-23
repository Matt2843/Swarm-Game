package com.swarmer.server;

import com.swarmer.server.Connection;
import com.swarmer.server.MotherShip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Matt on 03/16/2017.
 */
public class AcceptConnections extends Thread {

    private ServerSocket server;
    private Socket connection;
    private final int port;

    private boolean running = true;

    public AcceptConnections(int port) {
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
            Connection newCon = new Connection(connection, MotherShip.getNextNode());
            newCon.start();
        }
    }
    // TODO: First client server contact, Establish connection

}
