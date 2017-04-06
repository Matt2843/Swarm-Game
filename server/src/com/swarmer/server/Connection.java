package com.swarmer.server;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.LobbyNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Socket connection = null;
	private String clientIp = "";

	private ServerNode attachedNode;

	private Player player = new Player();

	public Connection(Socket connection, ServerNode attachedNode) throws IOException {
		this.connection = connection;
		this.attachedNode = attachedNode;
		clientIp = connection.getRemoteSocketAddress().toString();
		setupStreams();
	}

	@Override
	public void run() {
		Message message = null;
		do {
			try {
				message = (Message) input.readObject();
				System.out.println("Message Received + " + message.getOpcode());
				react(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			}
		} while(message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	private void react(Message message) throws IOException, OperationInWrongServerNodeException {
		Object receivedObject = message.getObject();
		switch (message.getOpcode()) {
			case 109: // Login, the object is a String[] containing (String username, String password) - TODO: should be an encrypted object.
				if(attachedNode.getDescription().equals("Authentication Node")) {
					String username = ((String[])receivedObject)[0];
					char[] password = ((String[])receivedObject)[0].toCharArray();
					boolean evaluate = ((AuthenticationNode) attachedNode).authenticateUser(username, password);
					if(evaluate) {
						player.setAlias(username);
						sendMessage(new Message(110));
					} else {
						sendMessage(new Message(111));
					}
				} else {
					throw new OperationInWrongServerNodeException("Attempted to invoke function calls in a wrong type of ServerNode");
				}
				// TODO: Figure out how to add connection to the correct list and remove when left.
				attachedNode.addConnection(this);
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
				String[] broadcastObject = {(String) message.getObject(), player.getAlias()};
				attachedNode.broadcast(new Message(304, broadcastObject));
				break;
			default:
				break;
		}
	}

	public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}

	private void cleanUp() {
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getClientIp() {
		return clientIp;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public ServerNode getAttachedNode() {
		return attachedNode;
	}
}
