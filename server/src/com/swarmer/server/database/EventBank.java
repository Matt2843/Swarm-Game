package com.swarmer.server.database;

import com.swarmer.shared.communication.PlayerInformation;
import com.swarmer.shared.communication.Product;
import com.swarmer.shared.exceptions.PlayerAlreadyExistsException;
import com.swarmer.shared.exceptions.PlayerNotFoundException;
import com.swarmer.shared.resources.Food;
import com.swarmer.shared.resources.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 03/16/2017.
 */
public class EventBank implements Serializable {

    private HashMap<PlayerInformation, HashMap<String, Resource>> playerResourceData;

    public EventBank() {
        playerResourceData = new HashMap<>();
    }

    public boolean purchase(final PlayerInformation playerInformation, Product desiredProduct) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(playerInformation)) {
            for(Map.Entry<Resource, Integer> entry : desiredProduct.getCost().entrySet()) {
                if(playerHasResource(playerInformation, entry.getKey())) {
                    if(playerResourceData.get(playerInformation).get(entry.getKey().getType()).getQuantity() < entry.getValue()) {
                        return false;
                    }
                } else return false;
            }
            return true;
        } else {
            throw new PlayerNotFoundException("Failed purchase: The player information does not exist in the database");
        }
    }

    public void removePlayer(PlayerInformation playerInformation) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(playerInformation)) {
            playerResourceData.remove(playerInformation);
        } else {
            throw new PlayerNotFoundException("Failed to remove player: The player information does not exist in the database");
        }
    }

    public void addNewPlayer(PlayerInformation playerInformation) throws PlayerAlreadyExistsException {
        if(playerResourceData.containsKey(playerInformation)) {
            throw new PlayerAlreadyExistsException("Failed to add new player: The player information already exists in the database");
        } else {
            playerResourceData.put(playerInformation, new HashMap<String, Resource>());
        }
    }

    public boolean playerHasResource(PlayerInformation playerInformation, Resource resource) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(playerInformation)) {
            if(playerResourceData.get(playerInformation).containsKey(resource)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new PlayerNotFoundException("Failed to test if player has resource: The player does not exist in the database");
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
