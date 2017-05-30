package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;

public class GameQueueEntry {
    private int freeSpots = 2;
    private ArrayList<Player> connectedPlayers;
    private boolean full = false;

    public GameQueueEntry(ArrayList<Player> players) {
        addPlayers(players);
    }

    public boolean hasFreeSpots(int playerCount) {
        return playerCount <= freeSpots;
    }

    public void addPlayers(ArrayList<Player> players) {
        connectedPlayers.addAll(players);
        freeSpots -= players.size();

        if (freeSpots == 0) {
            this.full = true;
        }
    }

    public boolean isFull() {
        return this.full;
    }

    public ArrayList<Player> getPlayers() {
        return connectedPlayers;
    }
}
