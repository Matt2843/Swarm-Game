package com.swarmer.server;

import com.swarmer.server.database.ServerDatabase;
import com.swarmer.server.nodes.GameNode;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.exceptions.PlayerAlreadyExistsException;
import com.swarmer.shared.exceptions.PlayerNotFoundException;
import com.swarmer.shared.resources.Food;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class Connection extends Thread {
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Socket client = null;
	private String clientIP = "";

	private Player player;

	public Connection(Socket connection) throws IOException {
		client = connection;
		clientIP = client.getRemoteSocketAddress().toString();
		player = new Player(UUID.randomUUID().toString(), 333);
		setupStreams();
	}

	@Override
	public void run() {
		Message message = null;
		try {
			do {
				message = (Message) input.readObject();
				try {
					react(message);
				} catch (PlayerAlreadyExistsException e) {
					e.printStackTrace();
				}
			} while(!message.getMessage().equals("STOPSERVER"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void react(Message message) throws PlayerNotFoundException, PlayerAlreadyExistsException {
		switch (message.getMessage()) {
			case "JOIN":
				((GameNode)ServerDatabase.serverNodes.get(1234)).addClient(this);
				((GameNode)ServerDatabase.serverNodes.get(1234)).getEventBank().addNewPlayer(player);
				break;
			case "ADD5":
				((GameNode)ServerDatabase.serverNodes.get(1234)).getEventBank().addResourceToPlayer(player, new Food(), 5);
				break;
			case "SUB5":
				((GameNode)ServerDatabase.serverNodes.get(1234)).getEventBank().removeResourceFromPlayer(player, new Food(), 5);
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
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
	}
	
	public String getClientIP() {
		return clientIP;
	}
}
