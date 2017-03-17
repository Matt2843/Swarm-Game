package com.swarmer.server;

import com.swarmer.server.database.ServerDatabase;
import com.swarmer.server.nodes.EventNode;
import com.swarmer.shared.communication.Message;

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

	public void createEventNode(String eventNodeDescription, int eventNodeId) {
		EventNode eventNode = new EventNode(eventNodeDescription, eventNodeId);
		eventNode.start();
		ServerDatabase.serverNodes.put(eventNodeId, eventNode);
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
		ms.createEventNode("Test Node", 1234);
	}
}
