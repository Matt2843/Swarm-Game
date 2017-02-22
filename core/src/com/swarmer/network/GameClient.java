package com.swarmer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient extends Thread {
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private String host = "localhost";
	private int port = 1234;
	
	private Socket client; 
	
	public GameClient() {
		try {
			client = new Socket(InetAddress.getByName(host), port);
			setupStreams();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
	}

	@Override
	public void run() {
		// TODO:  while true loop,
		// cleanUp method
		// connection commands
	}
	
	
	public void cleanUp() {
		
	}
}
