package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Queue implements Runnable {

    private static HashMap<String, ArrayList<GameQueueEntry>> queueEntriesMap = new HashMap<>();

    public Queue() {
        queueEntriesMap.put("LOW", new ArrayList<GameQueueEntry>());
        queueEntriesMap.put("MID", new ArrayList<GameQueueEntry>());
        queueEntriesMap.put("HIGH", new ArrayList<GameQueueEntry>());


    }

    @Override
    public void run() {
        for (Map.Entry<String, ArrayList<GameQueueEntry>> queueEntryMap : queueEntriesMap.entrySet()) {
            for (GameQueueEntry queueEntry : queueEntryMap.getValue()) {
                
            }
        }
    }

    public void findMatch(ArrayList<Player> players) {

        int ranking = getRanking(players);

        String skillGroup = "HIGH";
        if (ranking < 3000) {
            skillGroup = "MID";
        }
        if (ranking < 1000) {
            skillGroup = "LOW";
        }

        boolean foundMatch = false;

        for (GameQueueEntry queueEntry : queueEntriesMap.get(skillGroup)) {
            if (queueEntry.hasFreeSpots(players.size())) {
                queueEntry.addPlayers(players);
                foundMatch = true;
            }
        }

        if (!foundMatch) {
            new GameQueueEntry(players);
        }
    }

    private int getRanking(ArrayList<Player> players) {
        int totalRating = 0;

        for (Player player : players) {
            totalRating += player.getRating();
        }

        return totalRating / players.size();
    }
}
