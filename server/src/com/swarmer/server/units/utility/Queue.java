package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Message;
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
        while (true) {
            System.out.println("Leder efter et spil");

            for (Map.Entry<String, ArrayList<GameQueueEntry>> queueEntryMap : queueEntriesMap.entrySet()) {
                for (GameQueueEntry queueEntry : queueEntryMap.getValue()) {
                    if (queueEntry.isFull()) {
                        System.out.println("Fundet et spil! :)");
                    }
                }
            }

            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
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
            GameQueueEntry queueEntry = new GameQueueEntry(players);
            queueEntriesMap.get(skillGroup).add(queueEntry);
        }
    }

    private int getRanking(ArrayList<Player> players) {
        int totalRating = 0;

        for (Player player : players) {
            totalRating += player.getRating();
        }

        return totalRating / players.size();
    }

//    LocationInformation locationInformation = findPlayerLocationInformation(player.getUsername());
//
//							System.out.println("Sending request to " + player.getUsername());
//    sendTo(locationInformation, null, new Message(13372));
}
