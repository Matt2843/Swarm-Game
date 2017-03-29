package com.swarmer.server;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

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

	private Player player;

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
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println(message.getMessage());
			react(message);
		} while(true); // TODO: CHANGE STOP CONDITION.
	}

	private void react(Message message) {
		switch (message.getMessage()) {
			case "test":
				((AuthenticationNode) attachedNode).authenticateUser();
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
	
	public String getClientIp() {
		return clientIp;
	}
}
