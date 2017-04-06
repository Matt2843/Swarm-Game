package com.swarmer.server.nodes;

import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;

/**
 * Created by Matt on 08-03-2017.
 */
public class AuthenticationNode extends ServerNode {

	// TODO: USE THIS IN FUTURE
	private SecureTCPConnection secureClientConnection;
	public static TCPConnection clientConnection;

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
		// secureClientConnection = new SecureTCPConnection(connection, authenticationProtocol);

		clientConnection = new TCPConnection(connection, authenticationProtocol);
		new Thread(clientConnection).start();
	}

	public static void createUser(String username, String password) throws NoSuchAlgorithmException, IOException {
		byte[] salt = new byte[32];
		SecureRandom.getInstanceStrong().nextBytes(salt);
		String hashedPassword = HashingTools.hashPassword(password.toCharArray(), salt);
		motherShipConnection.sendMessage(new Message(3, new String[] {username, hashedPassword, HashingTools.bytesToHex(salt)}));
	}

	public static void authenticateUser(String username, String password) throws IOException {
		motherShipConnection.sendMessage(new Message(4, new String[] {username, password}));


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
