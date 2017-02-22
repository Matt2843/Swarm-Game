package com.swarmer.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private ServerSocket server;
	private Socket connection;
	private final int port;
	
	public Server(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		
	}
}
