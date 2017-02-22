package com.swarmer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
	
	private ArrayList<Connection> allConnections;
	
	private ServerSocket server;
	private Socket connection;
	private final int port;
	
	private boolean running = true;
	
	public Server(int port) {
		this.port = port;
		allConnections = new ArrayList<Connection>();
	}
	
	@Override
	public void run() {
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
		} catch(IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			cleanUp();
		}
		
	}

	private void waitForConnection() throws IOException, InterruptedException {
		while(running) {
			System.out.println("Awaiting connection.. :)");
			connection = server.accept();
			Connection newClient = new Connection(connection);
			newClient.start();
			allConnections.add(newClient);
			
			Thread.sleep(50);
		}
	}
	
	private void cleanUp() {
		for(Connection c : allConnections) {
			try {
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Server(1234).start();
	}
	
	
	
	
	
}
