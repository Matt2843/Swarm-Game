package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.server.MotherShip;
import com.swarmer.shared.exceptions.CorruptedDatabaseException;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

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
        } catch (UnkownServerNodeException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CorruptedDatabaseException e) {
            e.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException, UnkownServerNodeException, SQLException, CorruptedDatabaseException {
        while(running) {
            connection = server.accept();
            Connection newCon = new Connection(connection, MotherShip.findNextPrimitiveNode(this));
            newCon.start();
        }
    }

    @Override public String generateInsertQuery() {
        return null;
    }

    @Override public String getDescription() {
        return "Greeting Node";
    }

    @Override public String nextInPrimitiveChain() {
        return "authentication_nodes";
    }

}
