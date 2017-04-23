package com.swarmer.server;

import com.swarmer.server.protocols.DatabaseControllerProtocol;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class DatabaseController {

	public static MySQLConnection mySQLConnection = new MySQLConnection("localhost", 3306);
	private final DatabaseControllerProtocol mothershipProtocol = new DatabaseControllerProtocol();

	public static KeyPair KEY = null;
	private ServerSocket serverSocket;
	private Socket connection;
	private final int port;

	public DatabaseController(int port) {
		this.port = port;
		generateKeys();
		clearDatabase();
		try {
			awaitConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KEY = kpg.generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void clearDatabase() {
		try {
			mySQLConnection.sqlExecute("DELETE FROM access_units");
			mySQLConnection.sqlExecute("DELETE FROM authentication_units");
			mySQLConnection.sqlExecute("DELETE FROM coordination_units");
			mySQLConnection.sqlExecute("DELETE FROM lobby_units");
			mySQLConnection.sqlExecute("DELETE FROM game_units");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void awaitConnection() throws IOException {
		serverSocket = new ServerSocket(port);
		while(true) {
			connection = serverSocket.accept();
			TCPConnection tcpConnection = new TCPConnection(connection, mothershipProtocol);
			tcpConnection.start();
		}
	}

	public static void main(String[] args) {
		DatabaseController databaseController = new DatabaseController(ServerUnit.DATABASE_CONTROLLER_TCP_PORT);
	}
}
