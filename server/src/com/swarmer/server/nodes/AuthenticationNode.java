package com.swarmer.server.nodes;

import com.swarmer.server.MotherShip;
import com.swarmer.server.security.HashingTools;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 08-03-2017.
 */
public class AuthenticationNode extends ServerNode {

    public AuthenticationNode() {
        addNodeToMothership();
    }

    private boolean userExists(String username) throws SQLException {
        return MotherShip.sqlExecuteQuery("SELECT 1 FROM users WHERE username=?", username).last();
    }

    public boolean createUser(String username, char[] password) {
        byte[] salt = new byte[32];
        try {
            SecureRandom.getInstanceStrong().nextBytes(salt);
            String hashedPassword = HashingTools.hashPassword(password, salt);

            if(!userExists(username)) {
                MotherShip.sqlExecute("INSERT INTO users (id, username, password, password_salt) VALUES ('" + UUID.randomUUID().toString() + "','" + "?" + "','" + hashedPassword + "','" + HashingTools.bytesToHex(salt) + "')", username);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean authenticateUser(String username, char[] password) {
        try {
            if(userExists(username)) {
                String saltHex = MotherShip.sqlExecuteQueryToString("SELECT password_salt FROM users WHERE username='" + username + "'");
                byte[] saltBytes = HashingTools.hexToBytes(saltHex);
                String hashedPassword = HashingTools.hashPassword(password, saltBytes);
                String hashedPasswordFromDatabase = MotherShip.sqlExecuteQueryToString("SELECT password FROM users WHERE username='" + username + "'");
                if(hashedPassword.equals(hashedPasswordFromDatabase)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public String generateInsertQuery() {
        return "INSERT INTO authentication_nodes (id, user_count) VALUES ('" + getNodeId() + "'," + usersConnected + ")";
    }

    @Override
    public String getDescription() {
        return "Authentication Node";
    }

    @Override public String nextInPrimitiveChain() {
        return "lobby_nodes";
    }

}
