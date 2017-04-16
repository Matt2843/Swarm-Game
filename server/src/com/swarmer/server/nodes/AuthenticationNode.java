package com.swarmer.server.nodes;

import com.swarmer.server.MotherShipCallable2;
import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

public class AuthenticationNode extends ServerNode {

	private static final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol();

	protected AuthenticationNode(int port) {
		super(port);
	}

	public static boolean createUser(Message message) throws ExecutionException, InterruptedException, IOException {
		Object[] receivedObject = (Object[]) message.getObject();
		String username = (String) receivedObject[0];
		char[] password = (char[]) receivedObject[1];
		byte[] salt = new byte[32];
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String hashedPassword = HashingTools.hashPassword(password, salt);
		MotherShipCallable2 msc = new MotherShipCallable2(new Message(message.getOpcode(), new String[] {username, hashedPassword, HashingTools.bytesToHex(salt)}));
		return (boolean) msc.getFutureResult().getObject();
	}

	public static boolean authenticateUser(Message message) {
		String username = (String) ((Object[])message.getObject())[0];
		char[] password = (char[]) ((Object[])message.getObject())[1];
		MotherShipCallable2 msc = new MotherShipCallable2(new Message(message.getOpcode(), username));
		Message foundCredentials = msc.getFutureResult(); // password = [0], password_salt = [1]
		if(foundCredentials.getObject() == null) {
			return false;
		} else {
			String hashedPasswordInDB = ((String[])foundCredentials.getObject())[0];
			String saltInHex = ((String[])foundCredentials.getObject())[1];
			byte[] salt = HashingTools.hexToBytes(saltInHex);
			String passwordCheck = HashingTools.hashPassword(password, salt);
			if(passwordCheck.equals(hashedPasswordInDB)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	protected void handleConnection(Socket connection) throws IOException {
		TCPConnection clientConnection = new TCPConnection(connection, authenticationProtocol);
		clientConnection.start();
	}

	@Override
	public String getDescription() {
		return "authentication_nodes";
	}

	public static void main(String[] args) {
		new AuthenticationNode(1112);
	}
}