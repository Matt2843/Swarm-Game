package com.swarmer.server.units;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.protocols.LobbyProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.Lobby;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LobbyUnit extends ServerUnit {

    private final LobbyProtocol lobbyProtocol = new LobbyProtocol(this);
    private static HashMap<String, Lobby> hostedLobbies = new HashMap<>();

    private LobbyUnit() {
        super();
    }

    public void broadcastMessageToLobby(Message message, Player sender) throws IOException {
        String lobbyId = ((String[])message.getObject())[0];
        if(hostedLobbies.containsKey(lobbyId)) {
            for(Player player : hostedLobbies.get(lobbyId).getConnectedUsers()) {
                sendToPlayer(player.getUsername(), new Message(301, new String[] {((String[])message.getObject())[1], sender.getUsername()}));
            }
        }
    }

    public String createLobby(Player lobbyOwner) throws IOException {
        String lobbyID = UUID.randomUUID().toString();
        Lobby lobby = new Lobby();
        if(!addLobby(lobbyID, lobby))
            return null;
        joinLobby(lobbyID, lobbyOwner);
        return lobbyID;
    }

    public void startGame(String id) throws IOException {
        if(hostedLobbies.containsKey(id)) {
            ArrayList<Player> players = hostedLobbies.get(id).getConnectedUsers();

            new CoordinationUnitCallable(new Message(13371, players)).getFutureResult();
        }
    }

    public void joinLobby(String lobbyId, Player player) throws IOException {
        if(hostedLobbies.containsKey(lobbyId)) {
            hostedLobbies.get(lobbyId).connectUser(player);
            for(Player p : hostedLobbies.get(lobbyId).getConnectedUsers()) {
                if(!p.equals(player))
                    sendToPlayer(p.getUsername(), new Message(302, player));
            }
            for(Player p : hostedLobbies.get(lobbyId).getConnectedUsers()) {
                sendToPlayer(player.getUsername(), new Message(302, p));
            }
        }
    }

    private boolean addLobby(String lobbyID, Lobby lobby) {
        if(!hostedLobbies.containsKey(lobbyID)) {
            hostedLobbies.put(lobbyID, lobby);
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
