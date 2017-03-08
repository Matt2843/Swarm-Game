package com.swarmer.server;

import com.swarmer.shared.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Socket client = null;
	private String clientIP = "";

	@Override
	public void run() {
		com.swarmer.shared.Message message = null;
		try {
			do {
				message = (com.swarmer.shared.Message) input.readObject();
				System.out.println(message.getMessage());
			} while(!message.getMessage().equals("STOPSERVER"));
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public Connection(Socket connection) throws IOException {
		client = connection;
		clientIP = client.getRemoteSocketAddress().toString();
		setupStreams();
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
