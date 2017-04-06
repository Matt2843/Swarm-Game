package com.swarmer.server;

import com.swarmer.server.protocols.MotherShipProtocol;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class MotherShip {

	public static MySQLConnection mySQLConnection = new MySQLConnection("localhost", 3306);

	private final MotherShipProtocol mothershipProtocol = new MotherShipProtocol();

	private ServerSocket server;
	private Socket connection;

	private final int port;

	public MotherShip(int port) throws IOException {
		this.port = port;
		mySQLConnection = new MySQLConnection("localhost", 3306);
		clearDatabase();
		awaitConnection();
	}

	private void clearDatabase() {
		try {
			mySQLConnection.sqlExecute("DELETE FROM authentication_nodes");
			mySQLConnection.sqlExecute("DELETE FROM lobby_nodes");
			mySQLConnection.sqlExecute("DELETE FROM game_nodes");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void awaitConnection() throws IOException {
		server = new ServerSocket(port);
		while(true) {
			connection = server.accept();
			TCPConnection tcpConnection = new TCPConnection(connection, mothershipProtocol);
			new Thread(tcpConnection).start();
		}
	}

	public static void main(String[] args) {
		try {
			MotherShip ms = new MotherShip(1110);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
