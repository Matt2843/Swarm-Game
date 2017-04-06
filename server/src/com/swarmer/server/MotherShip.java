package com.swarmer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class MotherShip {

	public MySQLConnection mySQLConnection;

	private ServerSocket server;
	private Socket connection;
	private final int port;

	public MotherShip(int port) {
		this.port = port;
		mySQLConnection = new MySQLConnection("localhost", 3306);
		awaitConnection();
	}

	private void awaitConnection() {
		try {
			server = new ServerSocket(port);
			while(true) {
				connection = server.accept();
				// TODO: Start a thread Connection and re-route this.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MotherShip ms = new MotherShip(1234);

		// Clear db?
		try {
			ms.mySQLConnection.sqlExecute("DELETE FROM authentication_nodes");
			ms.mySQLConnection.sqlExecute("DELETE FROM lobby_nodes");
			ms.mySQLConnection.sqlExecute("DELETE FROM game_nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
