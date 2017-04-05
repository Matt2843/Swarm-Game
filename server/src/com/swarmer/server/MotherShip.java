package com.swarmer.server;

import com.swarmer.server.nodes.*;
import com.swarmer.shared.exceptions.CorruptedDatabaseException;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

public class MotherShip {

	private static HashMap<String, ServerNode> allActiveNodes;

	private static Connection mySqlConnection;
	private final String sqlServerIp;
	private final int port;

	public MotherShip(String sqlServerIp, int port) {
		this.sqlServerIp = sqlServerIp;
		this.port = port;

		allActiveNodes = new HashMap<>();
		try {
			mySqlConnection = DriverManager.getConnection("jdbc:mysql://" + sqlServerIp + ":" + port + "/swarmer?serverTimezone=Europe/Copenhagen", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		new GreetingNode(1111).start();
	}

	public static void sqlExecute(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		statement.execute();
	}

	public static String sqlExecuteQueryToString(String query, String... values) throws SQLException {
		ResultSet queryResult = sqlExecuteQuery(query, values);
		String result = "";
		while(queryResult.next()) {
			result = queryResult.getString(1);
		}
		return result;
	}

	public static ResultSet sqlExecuteQuery(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		return statement.executeQuery();
	}

	/**
	 * A method used to return the next primitive server node in the initial connection chain.
	 * @param currentNode a ServerNode which the client is currently connected to.
	 * @return returns the next primitive server node in the initial connection chain.
	 * @throws SQLException if query fails
	 * @throws CorruptedDatabaseException if the sql database is not up to date.
	 */
	public static ServerNode findNextPrimitiveNode(ServerNode currentNode) throws SQLException, CorruptedDatabaseException {
		if(currentNode instanceof LobbyNode || currentNode instanceof GameNode) return null;

		String queryStringValue = sqlExecuteQueryToString("SELECT * FROM " + currentNode.nextInPrimitiveChain() + " ORDER BY user_count ASC LIMIT 1");

		if(allActiveNodes.containsKey(queryStringValue)) {
			return allActiveNodes.get(queryStringValue);
		} else throw new CorruptedDatabaseException();
	}

	public static void addNode(ServerNode node) throws UnkownServerNodeException, SQLException {
		mySqlConnection.createStatement().executeUpdate(node.generateInsertQuery());
		allActiveNodes.put(node.getNodeId(), node);
	}

	public static void log(String message) {
		System.err.println("[MOTHERSHIP]: " + new Timestamp(Calendar.getInstance().getTime().getTime()) + message);
	}

	public static void main(String[] args) {
		MotherShip ms = new MotherShip("localhost", 3306);
		/*
		 * The following code sets up 2 default nodes (which are needed for the network architecture to work).
		 */

		/*
		 * TODO: This should not exist when using servers
		 *
		 * Refresh nodes at launch
		 */
		try {
			mySqlConnection.createStatement().execute("DELETE FROM authentication_nodes");
			mySqlConnection.createStatement().execute("DELETE FROM lobby_nodes");
			mySqlConnection.createStatement().execute("DELETE FROM game_nodes");

			new AuthenticationNode().start();
			new LobbyNode().start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
