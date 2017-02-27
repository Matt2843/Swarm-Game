package com.swarmer.server.database;

import com.swarmer.server.Connection;
import com.swarmer.server.utility.Player;
import com.swarmer.server.utility.Ant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matt on 02/27/2017.
 */
public class GameDatabase {

    public static ArrayList<Connection> connectedClients = new ArrayList<>();

    public static HashMap<Player, Ant> positionData;

    public GameDatabase() {
        positionData = new HashMap<>();
    }

}
