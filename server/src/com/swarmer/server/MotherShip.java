package com.swarmer.server;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.GreetingNode;
import com.swarmer.server.nodes.LobbyNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MotherShip extends Thread {

	private static java.sql.Connection mySqlConnection;
	private final String sqlServerIp;
	private final int port;

	public MotherShip(String sqlServerIp, int port) {
		this.sqlServerIp = sqlServerIp;
		this.port = port;
		try {
			mySqlConnection = DriverManager.getConnection("jdbc:mysql:/" + sqlServerIp + ":" + port + "/swarmer", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getNextNode() {

	}

	public static void addNode(ServerNode node) throws UnkownServerNodeException, SQLException {
		mySqlConnection.createStatement().executeUpdate(node.generateInsertQuery());
	}
	
	@Override public void run() {

	}

	public static void main(String[] args) {
		MotherShip ms = new MotherShip("localhost", 3306);
		ms.start();

		/**
		 * The following code sets up 3 default nodes (which are needed for the network architecture to work).
		 */

		GreetingNode defGreetingNode = new GreetingNode(1111);
		AuthenticationNode defAuthenticatioNode = new AuthenticationNode();
		LobbyNode defLobbyNode = new LobbyNode();
	}
}
