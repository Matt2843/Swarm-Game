package com.swarmer.server.units;

import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

public class AuthenticationUnit extends ServerUnit {

	private final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol(this);

	protected AuthenticationUnit() {
		super();
	}

	public static Player createUser(Message message) throws ExecutionException, InterruptedException, IOException {
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
		DatabaseControllerCallable databaseControllerCallable = new DatabaseControllerCallable(new Message(message.getOpcode(), new String[] {username, hashedPassword, HashingTools.bytesToHex(salt)}));
		if (databaseControllerCallable.getFutureResult().getObject() == null) {
			return null;
		}

		return (Player) databaseControllerCallable.getFutureResult().getObject();
	}

	public static Player authenticateUser(Message message) throws IOException {
		Object[] receivedObject = (Object[]) message.getObject();

		String username = (String) receivedObject[0];
		char[] password = (char[]) receivedObject[1];

		DatabaseControllerCallable databaseControllerCallable = new DatabaseControllerCallable(new Message(message.getOpcode(), username));
		Message returnedMessage = databaseControllerCallable.getFutureResult();
		if(returnedMessage.getObject() == null) {
			return null;
		}

		Player player = (Player) ((Object[])returnedMessage.getObject())[0];

		String hashedPasswordInDB = ((String[])((Object[])returnedMessage.getObject())[1])[0];
		String saltInHex = ((String[])((Object[])returnedMessage.getObject())[1])[1];

		byte[] salt = HashingTools.hexToBytes(saltInHex);
		String passwordCheck = HashingTools.hashPassword(password, salt);

		if(!passwordCheck.equals(hashedPasswordInDB)) {
			return null;
		}

		return player;
	}

	@Override public int getPort() {
		return ServerUnit.AUTHENTICATION_UNIT_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return authenticationProtocol;
	}

	@Override
	public String getDescription() {
		return "authentication_units";
	}

	public static void main(String[] args) {
		new AuthenticationUnit();
	}
}