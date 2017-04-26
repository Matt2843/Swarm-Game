package com.swarmer.server;

import com.swarmer.server.protocols.DatabaseControllerProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class DatabaseController extends Unit {

	public static MySQLConnection mySQLConnection = new MySQLConnection("localhost", 3306);
	private final DatabaseControllerProtocol mothershipProtocol = new DatabaseControllerProtocol();

	public DatabaseController() {
		super();
		clearDatabase();
	}

	@Override public int getPort() {
		return DATABASE_CONTROLLER_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return mothershipProtocol;
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

	public static void main(String[] args) {
		DatabaseController databaseController = new DatabaseController();
	}
}
