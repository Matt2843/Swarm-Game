package com.swarmer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient extends Thread {
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private String host = "localhost";
	private int port = 1234;
	
	private Socket client; 
	
	public GameClient() {
		
	}

	public GameClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			connectToService();
			setupStreams();
			sendMessage(new Message("Hello"));
			whileConnected();
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
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
	
	private void connectToService() throws UnknownHostException, IOException {
		client = new Socket(InetAddress.getByName(host), port);
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
	}
	
	
	public void cleanUp() {
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new GameClient().start();
	}
}
