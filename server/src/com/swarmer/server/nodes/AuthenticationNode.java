package com.swarmer.server.nodes;

import com.swarmer.server.MySQLConnection;
import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 08-03-2017.
 */
public class AuthenticationNode extends ServerNode {

	// TODO: USE THIS IN FUTURE
	private SecureTCPConnection secureTCPConnection;
	private TCPConnection tcpConnection;

	private final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol();

	protected AuthenticationNode(int port) throws IOException {
		super(port);
	}

	@Override protected void setupMotherShipConnection() throws IOException {
		motherShipConnection = new TCPConnection(new Socket("localhost", 1110), authenticationProtocol);
		new Thread(motherShipConnection).start();
	}

	@Override protected void handleConnection(Socket connection) throws IOException {
		// TODO: use this in the future when the secure connection is tested.
		// secureTCPConnection = new SecureTCPConnection(connection, authenticationProtocol);

		tcpConnection = new TCPConnection(connection, authenticationProtocol);
		new Thread(tcpConnection).start();
	}

	private static boolean userExists(String username) throws SQLException {

		return mySQLConnection.sqlExecuteQuery("SELECT 1 FROM users WHERE username = ?", username).last();
	}

	public static boolean createUser(String username, char[] password) {
		byte[] salt = new byte[32];
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
			String hashedPassword = HashingTools.hashPassword(password, salt);

			if(!userExists(username)) {
				mySQLConnection.sqlExecute("INSERT INTO users (id, username, password, password_salt) VALUES ('" + UUID.randomUUID().toString() + "',?,'" + hashedPassword + "','" + HashingTools.bytesToHex(salt) + "')", username);
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean authenticateUser(String username, char[] password) {
		try {
			if(userExists(username)) {
				String saltHex = mySQLConnection.sqlExecuteQueryToString("SELECT password_salt FROM users WHERE username = ?", username);
				byte[] saltBytes = HashingTools.hexToBytes(saltHex);
				String hashedPassword = HashingTools.hashPassword(password, saltBytes);
				String hashedPasswordFromDatabase = mySQLConnection.sqlExecuteQueryToString("SELECT password FROM users WHERE username = ?", username);
				if(hashedPassword.equals(hashedPasswordFromDatabase)) {
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override public String generateInsertQuery() {
		return "INSERT INTO authentication_nodes (id, user_count) VALUES ('" + getNodeUUID() + "'," + usersConnected + ")";
	}

	@Override public String getDescription() {
		return "Authentication Node";
	}

	public static void main(String[] args) {
		try {
			AuthenticationNode authenticationNode = new AuthenticationNode(1111);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
