package com.swarmer.server.units;

import com.swarmer.server.protocols.LobbyProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.Lobby;
import com.swarmer.shared.communication.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyUnit extends ServerUnit {

    private final LobbyProtocol lobbyProtocol = new LobbyProtocol(this);
    private static HashMap<String, Lobby> hostedLobbies = new HashMap<>();

    protected LobbyUnit() {
        super();
    }

    public static String createLobby(Player lobbyOwner) {
        String lobbyID = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyID, lobbyOwner);
        if(addLobby(lobbyID, lobby))
            return lobbyID;
        return null;
    }

    private static boolean addLobby(String lobbyID, Lobby lobby) {
        if(!hostedLobbies.containsKey(lobbyID)) {
            hostedLobbies.put(lobbyID, lobby);
            return true;
        }
        return false;
    }

    public static boolean removeLobby(String lobbyID) {
        if(hostedLobbies.containsKey(lobbyID)) {
            hostedLobbies.remove(lobbyID);
            return true;
        }
        return false;
    }

    @Override public int getPort() {
        return ServerUnit.LOBBY_UNIT_TCP_PORT;
    }

    @Override protected ServerProtocol getProtocol() {
        return lobbyProtocol;
    }

    @Override public String getDescription() {
        return "lobby_units";
    }

    public static void main(String[] args) {
        new LobbyUnit();
    }

}
