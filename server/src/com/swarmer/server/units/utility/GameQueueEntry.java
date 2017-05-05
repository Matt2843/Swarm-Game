package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;

public class GameQueueEntry {
    private int freeSpots = 1;
    private ArrayList<Player> connectedPlayers = new ArrayList<>();

    public GameQueueEntry(ArrayList<Player> players) {
        addPlayers(players);
    }

    public boolean hasFreeSpots(int playerCount) {
        return playerCount <= freeSpots;
    }

    public void addPlayers(ArrayList<Player> players) {
        connectedPlayers.addAll(players);

        freeSpots -= players.size();
    }

    public ArrayList<Player> getPlayers() {
        return connectedPlayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameQueueEntry that = (GameQueueEntry) o;

        return freeSpots == that.freeSpots && connectedPlayers.equals(that.connectedPlayers);
    }

    public boolean isFull() {
        return freeSpots == 0;
    }
}
