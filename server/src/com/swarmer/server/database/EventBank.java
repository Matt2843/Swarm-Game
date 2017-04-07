package com.swarmer.server.database;

import com.swarmer.shared.communication.Player;
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

    private HashMap<Player, HashMap<String, Resource>> playerResourceData;

    public EventBank() {
        playerResourceData = new HashMap<>();
    }

    public boolean purchase(final Player player, Product desiredProduct) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            for(Map.Entry<Resource, Integer> entry : desiredProduct.getCost().entrySet()) {
                if(playerHasResource(player, entry.getKey())) {
                    if(playerResourceData.get(player).get(entry.getKey().getType()).getQuantity() < entry.getValue()) {
                        return false;
                    }
                } else return false;
            }
            return true;
        } else {
            throw new PlayerNotFoundException("Failed purchase: The player information does not exist in the database");
        }
    }

    public void removePlayer(Player player) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            playerResourceData.remove(player);
        } else {
            throw new PlayerNotFoundException("Failed to remove player: The player information does not exist in the database");
        }
    }

    public void addNewPlayer(Player player) throws PlayerAlreadyExistsException {
        if(playerResourceData.containsKey(player)) {
            throw new PlayerAlreadyExistsException("Failed to add new player: The player information already exists in the database");
        } else {
            playerResourceData.put(player, new HashMap<String, Resource>());
        }
    }

    public boolean playerHasResource(Player player, Resource resource) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            if(playerResourceData.get(player).containsKey(resource)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new PlayerNotFoundException("Failed to test if player has resource: The player does not exist in the database");
        }
    }

    public void removeResourceFromPlayer(Player player, Resource resource, int quantity) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            if(playerResourceData.get(player).get(resource.getType()).getQuantity() > quantity) {
                playerResourceData.get(player).get(resource.getType()).removeQuantity(quantity);
            } else playerResourceData.get(player).get(resource.getType()).setQuantity(0);
            System.out.println(player.getUsername() + " has: " + playerResourceData.get(player).get(resource.getType()).getQuantity() + " " + resource.getType());
        } else {
            throw new PlayerNotFoundException("The player information given does not match any player information in the database.");
        }
    }

    public void addResourceToPlayer(Player player, Resource resource, int quantity) throws PlayerNotFoundException {
        if(playerResourceData.containsKey(player)) {
            if(playerResourceData.get(player).containsValue(resource)) {
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
            System.out.println(player.getUsername() + " has: " + playerResourceData.get(player).get(resource.getType()).getQuantity() + " " + resource.getType());
        } else {
            throw new PlayerNotFoundException("The player information given does not match any player information in the database");
        }
    }
}
