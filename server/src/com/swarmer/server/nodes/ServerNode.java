package com.swarmer.server.nodes;

import com.swarmer.server.MySQLConnection;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int usersConnected = 0;

    protected ServerSocket serverSocket;
    protected Socket connection;

    protected MySQLConnection mySQLConnection = new MySQLConnection("localhost", 3306);

    private final String nodeUUID = UUID.randomUUID().toString();

    protected ServerNode(int port) throws IOException {
        initializeServerSocket(port);
        notifyMySQLServer();
        awaitConnection(connection);
    }

    protected void initializeServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    protected void notifyMySQLServer() {
        try {
            mySQLConnection.sqlExecute(generateInsertQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void awaitConnection(Socket connection) throws IOException {
        while(true) {
            connection = serverSocket.accept();
            handleConnection(connection);
        }
    }

    public abstract String generateInsertQuery();
    public abstract String getDescription();

    protected abstract void handleConnection(Socket connection) throws IOException;

    public String getNodeUUID() {
        return nodeUUID;
    }

    @Override
    public String toString() {
        return super.toString() + " -- ServerNode{" +
                "nodeId='" + nodeUUID + '\'' + " " + getDescription() +
                '}';
    }
}
