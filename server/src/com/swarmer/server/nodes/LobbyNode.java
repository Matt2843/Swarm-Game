package com.swarmer.server.nodes;

import com.swarmer.server.protocols.LobbyProtocol;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyNode extends ServerNode {

    private final LobbyProtocol lobbyProtocol = new LobbyProtocol();

    protected LobbyNode(int port) {
        super(port);
    }

    @Override
    protected void handleConnection(Socket connection) throws IOException {
        TCPConnection tcpConnection = new TCPConnection(connection, lobbyProtocol);
        tcpConnection.start();
    }

    @Override
    public String getDescription() {
        return "lobby_nodes";
    }

    public static void main(String[] args) {
        new LobbyNode(1113);
    }

}
