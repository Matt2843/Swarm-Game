package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.server.database.EventBank;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matt on 03/16/2017.
 */
public class GameNode extends ServerNode {

    private ArrayList<Connection> connectedClients;
    private EventBank eventBank;

    @Override public void run() {
        eventBank = new EventBank();
        connectedClients = new ArrayList<>();
    }

    public void broadcast(Message message) throws IOException {
        for(Connection connection : connectedClients) {
            connection.sendMessage(message);
        }
    }

    public void addClient(Connection client) {
        if(!connectedClients.contains(client)) {
            connectedClients.add(client);
        }
        try {
            broadcast(new Message("Client: " + client.getClientIp() + " joined the event."));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(Connection client) {
        if(connectedClients.contains(client)) {
            connectedClients.remove(client);
        }
    }

    public EventBank getEventBank() {
        return eventBank;
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO game_nodes (id, user_count) VALUES ('" + getNodeId() + "'," + usersConnected + ")";
    }

    @Override
    public String getDescription() {
        return "GameNode";
    }
}
