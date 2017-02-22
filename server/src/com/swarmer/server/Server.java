package com.swarmer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private ServerSocket server;
	private Socket connection;
	private final int port;
	
	private boolean running = true;
	
	public Server(int port) {
		this.port = port;
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
			connection = server.accept();
			Connection newClient = new Connection(connection);
			newClient.start();
			
			Thread.sleep(50);
		}
	}
	
	private void cleanUp() {
		// TODO Auto-generated method stub
		
	}
}
