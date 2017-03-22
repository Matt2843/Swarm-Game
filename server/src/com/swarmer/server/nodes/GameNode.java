package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.server.database.EventBank;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matt on 03/16/2017.
 */
public class EventNode extends ServerNode {

    private ArrayList<Connection> connectedClients;

    private EventBank eventBank;

    public EventNode() {
        super();
    }

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
            broadcast(new Message("Client: " + client.getClientIP() + " joined the event."));
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


    @Override
    public String getDescription() {
        return "EventNode";
    }
}
