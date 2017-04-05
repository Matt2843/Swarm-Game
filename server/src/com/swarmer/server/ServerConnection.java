package com.swarmer.server;

import com.swarmer.server.nodes.LobbyNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.communication.Connection;
import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.SecureConnection;
import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Connection {

	private ServerNode attachedNode;

	public ServerConnection(Socket connection, ServerNode node) throws IOException {
		super(connection);
		attachedNode = node;
	}

	@Override protected void react(Message message) throws IOException, OperationInWrongServerNodeException {
		Object receivedObject = message.getObject();
		switch (message.getOpcode()) {
			case 109: // Login, the object is a String[] containing (String username, String password) - TODO: should be an encrypted object.
				if(attachedNode.getDescription().equals("Authentication Node")) {
					String username = ((String[])receivedObject)[0];
					char[] password = ((String[])receivedObject)[0].toCharArray();
					boolean evaluate = ((AuthenticationNode) attachedNode).authenticateUser(username, password);
					if(evaluate) {
						sendMessage(new Message(110));
					} else {
						sendMessage(new Message(111));
					}
				} else {
					throw new OperationInWrongServerNodeException("Attempted to invoke function calls in a wrong type of ServerNode");
				}
				break;
			case 201: // Create user, the object is a String[] containing (String username, String password) - TODO: should be an encrypted object.
				if(attachedNode.getDescription().equals("Authentication Node")) {
					String username = ((String[])receivedObject)[0];
					char[] password = ((String[])receivedObject)[0].toCharArray();
					boolean evaluate = ((AuthenticationNode) attachedNode).createUser(username, password);
					if(evaluate) {
						sendMessage(new Message(202));
					} else {
						sendMessage(new Message(203));
					}
				} else {
					throw new OperationInWrongServerNodeException("Attempted to invoke function calls in a wrong type of ServerNode");
				}
				break;
			case 301:
				if(attachedNode instanceof LobbyNode) {
					String[] broadcastObject = {(String) message.getObject(), player.getAlias()};
					attachedNode.broadcast(new Message(304, broadcastObject));
				}
				break;
			default:
				break;
		}
	}
}
