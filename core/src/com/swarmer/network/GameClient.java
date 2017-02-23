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
			sendMessage("Hello");
			whileConnected();
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String m) throws IOException {
		output.writeObject(m);
		output.flush();
	}
	
	public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
	}
	
	private void whileConnected() throws ClassNotFoundException, IOException {
		Message message;
		do {
			message = (Message) input.readObject();
			System.out.println(message.getMessage());
		} while(message.getMessage().contains("STOPCONNECTION"));
		
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
	
	public static void main(String[] args) {
		new GameClient();
	}
}
