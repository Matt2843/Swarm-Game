package com.swarmer.server;

import java.sql.*;

/**
 * Created by Matt on 04/06/2017.
 */
public class MySQLConnection {

	private static java.sql.Connection mySqlConnection;

	public MySQLConnection(String sqlServerIp, int port) {
		try {
			mySqlConnection = DriverManager.getConnection("jdbc:mysql://" + sqlServerIp + ":" + port + "/swarmer?serverTimezone=Europe/Copenhagen", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void sqlExecute(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		statement.execute();
	}

	public static ResultSet sqlExecuteQuery(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		return statement.executeQuery();
	}


	public static String sqlExecuteQueryToString(String query, String... values) throws SQLException {
		ResultSet queryResult = sqlExecuteQuery(query, values);
		String result = "";
		while(queryResult.next()) {
			result = queryResult.getString(1);
		}
		return result;
	}
}
