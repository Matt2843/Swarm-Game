package com.swarmer.server.nodes;

import com.swarmer.server.database.ServerDatabase;
import com.swarmer.shared.communication.Player;

/**
 * Created by Matt on 08-03-2017.
 */
public class AuthenticationNode extends ServerNode {

    public void addPlayer(Player player, String hashedPassword) {
        if(!ServerDatabase.playerDatabase.containsKey(player)) {
            ServerDatabase.playerDatabase.put(player, hashedPassword);
        }
    }

    public void authenticateUser() {
        // TODO: Authenticate user credentials
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO authentication_nodes (id, user_count) VALUES ('" + getNodeId() + "'," + usersConnected + ")";
    }

    @Override
    public String getDescription() {
        return "AuthenticationNode";
    }

}
