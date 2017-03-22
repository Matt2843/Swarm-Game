package com.swarmer.server;

import com.swarmer.server.database.ServerDatabase;
import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.GameNode;
import com.swarmer.server.nodes.GreetingNode;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MotherShip extends Thread {

	private ServerSocket server;
	private Socket connection;
	private final int port;
	
	private boolean running = true;
	
	public MotherShip(int port) {
		this.port = port;
		ServerDatabase.initializeDatabase();
	}
	
	@Override public void run() {
		try {
			server = new ServerSocket(port);
		} catch(IOException e) {
			e.printStackTrace();
		}
		startServer();
	}

	private void startServer() {
		try {
			waitForConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cleanUp();
		}
	}

	private void waitForConnection() throws IOException {
		while(running) {
			System.out.println("Awaiting connection.. :)");
			connection = server.accept();
			Connection newClient = new Connection(connection);
			newClient.start();
		}
	}
	
	private void cleanUp() {
		/*
		* TODO: implement cleanUp method of server class!! */
	}
	
	public static void main(String[] args) {
		MotherShip ms = new MotherShip(1234);
		ms.start();
		try {
			ServerDatabase.createServerNode(new GreetingNode());
			ServerDatabase.createServerNode(new AuthenticationNode());
			ServerDatabase.createServerNode(new GameNode());
		} catch (UnkownServerNodeException e) {
			e.printStackTrace();
		}
	}
}
