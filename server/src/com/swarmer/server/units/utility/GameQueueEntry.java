package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;

public class GameQueueEntry {
    private int freeSpots = 2;
    private ArrayList<Player> connectedPlayers;

    public GameQueueEntry(ArrayList<Player> players) {
        addPlayers(players);
    }

    public boolean hasFreeSpots(int playerCount) {
        return playerCount <= freeSpots;
    }

    public void addPlayers(ArrayList<Player> players) {
        connectedPlayers.addAll(players);
    }

    public ArrayList<Player> getPlayers() {
        return connectedPlayers;
    }
}
