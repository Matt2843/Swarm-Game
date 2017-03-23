package com.swarmer.server;

import com.swarmer.server.database.ServerDatabase;
import com.swarmer.server.nodes.*;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;

public class MotherShip extends Thread {

	public static java.sql.Connection mySqlConnection;

	private ServerSocket server;
	private Socket connection;
	private final int port;
	
	private boolean running = true;
	
	public MotherShip(int port) {
		this.port = port;
		try {

			mySqlConnection = DriverManager.getConnection("jdbc:mysql://90.184.152.225:3306/swarmer", "georg", "123");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addNode(ServerNode node) throws UnkownServerNodeException, SQLException {
		mySqlConnection.createStatement().executeUpdate(node.generateInsertQuery());
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


		LobbyNode ln = new LobbyNode();
		GreetingNode gn = new GreetingNode();
	}
}
