package com.swarmer.server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {

	private static java.sql.Connection mySqlConnection;

	public MySQLConnection(String sqlServerIp, int port) {
		try {
			mySqlConnection = DriverManager.getConnection("jdbc:mysql://" + sqlServerIp + ":" + port + "/swarmer?serverTimezone=Europe/Copenhagen", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sqlExecute(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		statement.execute();
	}

	public ResultSet sqlExecuteQuery(String query, String... values) throws SQLException {
		PreparedStatement statement = mySqlConnection.prepareStatement(query);
		for(int i = 0; i < values.length; i++) {
			statement.setString(i + 1, values[i]);
		}
		return statement.executeQuery();
	}


	public String sqlExecuteQueryToString(String query, String... values) throws SQLException {
		ResultSet queryResult = sqlExecuteQuery(query, values);
		String result = "";
		while(queryResult.next()) {
			result = queryResult.getString(1);
		}
		return result;
	}
}
