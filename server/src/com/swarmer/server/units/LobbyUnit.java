package com.swarmer.server.units;

import com.swarmer.server.units.utility.Lobby;
import com.swarmer.server.protocols.LobbyProtocol;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyUnit extends ServerUnit {

    private final LobbyProtocol lobbyProtocol = new LobbyProtocol(this);

    protected LobbyUnit(int port) {
        super(port);
    }

    public static String createLobby(Player lobbyOwner) {
        String lobbyID = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyID, lobbyOwner);

        return lobbyID;
    }

    @Override protected void handleConnection(Socket connection) throws IOException {
        TCPConnection tcpConnection = new TCPConnection(connection, lobbyProtocol);
        tcpConnection.start();
    }

    @Override public String getDescription() {
        return "lobby_units";
    }

    public static void main(String[] args) {
        new LobbyUnit(ServerUnit.LOBBY_UNIT_STCP_PORT);
    }

}
