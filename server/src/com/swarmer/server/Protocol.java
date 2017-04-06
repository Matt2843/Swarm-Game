package com.swarmer.server;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;

/**
 * Created by Matt on 06-04-2017.
 */
public class Protocol {

    public void react(Message message, Connection callerConnection) throws OperationInWrongServerNodeException, IOException {
        Player player = callerConnection.getPlayer();
        ServerNode attachedNode = callerConnection.getAttachedNode();
        switch (message.getOpcode()) {
            case 109: // Login, the object is a String[] containing (String username, String password) - TODO: should be an encrypted object.
                if(attachedNode.getDescription().equals("Authentication Node")) {
                    String username = ((String[]) message.getObject())[0];
                    char[] password = ((String[]) message.getObject())[0].toCharArray();
                    boolean evaluate = ((AuthenticationNode) attachedNode).authenticateUser(username, password);
                    if(evaluate) {
                        player.setAlias(username);
                        callerConnection.sendMessage(new Message(110));
                    } else {
                        callerConnection.sendMessage(new Message(111));
                    }
                } else {
                    throw new OperationInWrongServerNodeException("Attempted to invoke function calls in a wrong type of ServerNode");
                }
                // TODO: Figure out how to add connection to the correct list and remove when left.
                attachedNode.addConnection(callerConnection);
                break;
            case 201: // Create user, the object is a String[] containing (String username, String password) - TODO: should be an encrypted object.
                if(attachedNode.getDescription().equals("Authentication Node")) {
                    String username = ((String[]) message.getObject())[0];
                    char[] password = ((String[]) message.getObject())[0].toCharArray();
                    boolean evaluate = ((AuthenticationNode) attachedNode).createUser(username, password);
                    if(evaluate) {
                        callerConnection.sendMessage(new Message(202));
                    } else {
                        callerConnection.sendMessage(new Message(203));
                    }
                } else {
                    throw new OperationInWrongServerNodeException("Attempted to invoke function calls in a wrong type of ServerNode");
                }
                break;
            case 301:
                String[] broadcastObject = {(String) message.getObject(), player.getAlias()};
                attachedNode.broadcast(new Message(304, broadcastObject));
                break;
            default:
                break;
        }
    }
}
