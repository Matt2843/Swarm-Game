package com.swarmer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Socket client = null;
	private String clientIP = "";
	
	public Connection(Socket connection) throws IOException {
		client = connection;
		clientIP = client.getRemoteSocketAddress().toString();
		setupStreams();
	}
	
	@Override
	public void run() {
		Message message = null;
		try {
			do {
				message = (Message) input.readObject();
				System.out.println(message.getMessage());
			} while(!message.getMessage().equals("STOPSERVER"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
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
