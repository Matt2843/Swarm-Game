package com.swarmer.server.database;

import com.swarmer.shared.communication.PlayerInformation;
import com.swarmer.shared.communication.Product;
import com.swarmer.shared.exceptions.PlayerAlreadyExistsException;
import com.swarmer.shared.exceptions.PlayerNotFoundException;
import com.swarmer.shared.resources.Food;
import com.swarmer.shared.resources.Resource;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Matt on 03/16/2017.
 */
public class EventBank implements Serializable {

    private HashMap<PlayerInformation, HashMap<String, Resource>> playerResourceData;

    public EventBank() {
        playerResourceData = new HashMap<>();
    }
    
    public void purchase(PlayerInformation player, Product desiredProduct) {
        // TODO: Implement Product abstract class, and decide which products can be purchased.
        // TODO: Check if player has currency to purchase product
        // TODO: Add data structure for players inventory / stash.
    }

    public void removePlayer(PlayerInformation playerInformation) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(playerInformation)) {
            playerResourceData.remove(playerInformation);
        } else {
            throw new PlayerNotFoundException("The player information does not exist in the database");
        }
    }

    public void addNewPlayer(PlayerInformation playerInformation) throws PlayerAlreadyExistsException {
        if(playerResourceData.containsKey(playerInformation)) {
            throw new PlayerAlreadyExistsException("The player information already exists in the database");
        } else {
            playerResourceData.put(playerInformation, new HashMap<String, Resource>());
        }
    }

    public void removeResourceFromPlayer(PlayerInformation playerInformation, Resource resource, int quantity) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(playerInformation)) {
            playerResourceData.get(playerInformation).get(resource.getType()).removeQuantity(quantity);
        } else {
            throw new PlayerNotFoundException("The player information given does not match any player information in the database.");
        }
    }

    public void addResourceToPlayer(PlayerInformation player, Resource resource, int quantity) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            if(playerResourceData.get(player).values().contains(resource.getType())) { // Not sure about this check.
                playerResourceData.get(player).get(resource.getType()).addQuantity(quantity);
            } else {
                switch(resource.getType()) {
                    case "Food":
                        playerResourceData.get(player).put("Food", new Food(quantity));
                        break;
                    default:
                        break;
                }
            }
        } else {
            throw new PlayerNotFoundException("The player information given does not match any player information in the database");
        }
    }
}
