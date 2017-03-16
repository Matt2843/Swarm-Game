package com.swarmer.server.database;

import com.swarmer.shared.Food;
import com.swarmer.shared.PlayerInformation;
import com.swarmer.shared.exceptions.PlayerNotFoundException;
import com.swarmer.shared.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matt on 03/16/2017.
 */
public class EventBank {

    private HashMap<PlayerInformation, HashMap<String, Resource>> bankData;

    public EventBank() {
        bankData = new HashMap<>();
    }

    public void addResourceToPlayer(PlayerInformation player, Resource resource, int quantity) throws PlayerNotFoundException {
        if(bankData.containsKey(player)) {
            if(bankData.get(player).values().contains(resource.getType())) { // Not sure about this check.
                bankData.get(player).get(resource.getType()).addQuantity(quantity);
            } else {
                switch(resource.getType()) {
                    case "Food":
                        bankData.get(player).put("Food", new Food(quantity));
                        break;
                    default:
                        break;
                }
            }
        } else {
            throw new PlayerNotFoundException();
        }
    }
}
