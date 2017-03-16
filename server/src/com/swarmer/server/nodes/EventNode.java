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

    private int eventNodeId;
    private String eventNodeDescription;
    private ArrayList<Connection> connectedClients;

    private static EventBank eventBank;

    public EventNode(int eventNodeId, String eventNodeDescription) {
        this.eventNodeId = eventNodeId;
        this.eventNodeDescription = eventNodeDescription;
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
    }

    public void removeClient(Connection client) {
        if(connectedClients.contains(client)) {
            connectedClients.remove(client);
        }
    }

    @Override public String toString() {
        return "EventNode{" +
                "eventNodeId=" + eventNodeId +
                ", eventNodeDescription='" + eventNodeDescription + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventNode eventNode = (EventNode) o;

        if (eventNodeId != eventNode.eventNodeId) return false;
        return eventNodeDescription != null ? eventNodeDescription.equals(eventNode.eventNodeDescription) : eventNode.eventNodeDescription == null;
    }

}
