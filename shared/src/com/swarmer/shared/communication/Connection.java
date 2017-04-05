package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Connection extends Thread {
	
	protected ObjectInputStream input;
	protected ObjectOutputStream output;
	
	protected Socket connection = null;
	protected String clientIp = "";

	protected Player player = null;

	public Connection(Socket connection) throws IOException {
		this.connection = connection;
		clientIp = connection.getRemoteSocketAddress().toString();
		setupStreams();
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				message = (Message) input.readObject();
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

	abstract protected void react(Message message) throws IOException, OperationInWrongServerNodeException;

	public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
	}

	protected void setupStreams() throws IOException {
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
}
