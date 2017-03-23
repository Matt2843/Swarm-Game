package com.swarmer.server;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.LobbyNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MotherShip {

	private static java.sql.Connection mySqlConnection;
	private final String sqlServerIp;
	private final int port;

	public MotherShip(String sqlServerIp, int port) {
		new AcceptConnections(1111).start();
		this.sqlServerIp = sqlServerIp;
		this.port = port;
		try {
			mySqlConnection = DriverManager.getConnection("jdbc:mysql:/" + sqlServerIp + ":" + port + "/swarmer", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ServerNode getNextNode() {
		return null;
	}

	public static void addNode(ServerNode node) throws UnkownServerNodeException, SQLException {
		mySqlConnection.createStatement().executeUpdate(node.generateInsertQuery());
	}

	public static void main(String[] args) {
		MotherShip ms = new MotherShip("localhost", 3306);
		/**
		 * The following code sets up 2 default nodes (which are needed for the network architecture to work).
		 */

		AuthenticationNode defAuthenticatioNode = new AuthenticationNode();
		LobbyNode defLobbyNode = new LobbyNode();
	}
}
