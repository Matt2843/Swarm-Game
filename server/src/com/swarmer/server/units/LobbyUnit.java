package com.swarmer.server.units;

import com.swarmer.server.protocols.LobbyProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.Lobby;
import com.swarmer.shared.communication.Player;

import java.util.UUID;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyUnit extends ServerUnit {

    private final LobbyProtocol lobbyProtocol = new LobbyProtocol(this);

    protected LobbyUnit() {
        super();
    }

    @Override public int getPort() {
        return ServerUnit.LOBBY_UNIT_TCP_PORT;
    }

    @Override protected ServerProtocol getProtocol() {
        return lobbyProtocol;
    }

    public static String createLobby(Player lobbyOwner) {
        String lobbyID = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyID, lobbyOwner);

        return lobbyID;
    }

    @Override public String getDescription() {
        return "lobby_units";
    }

    public static void main(String[] args) {
        new LobbyUnit();
    }

}
