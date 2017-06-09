package com.swarmer.server.units;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.DatabaseControllerSecureCallable;
import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.IPGetter;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.SecureTCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

public class AuthenticationUnit extends ServerUnit {

	private final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol(this);

	public static SecureTCPConnection stcp = null;

	private static PublicKey DBCkey = null;

	protected AuthenticationUnit() {
		super();
		try {
			DatabaseControllerCallable databaseControllerCallable = new DatabaseControllerCallable(new Message(11111, KEY.getPublic()));
			DBCkey = (PublicKey) databaseControllerCallable.getFutureResult().getObject();
			System.out.println("DBCKEY VALUE: " + DBCkey);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static Player createUser(Message message) throws ExecutionException, InterruptedException, IOException {
		Object[] receivedObject = (Object[]) message.getObject();
		if(receivedObject == null) {
			return null;
		}
		String username = (String) receivedObject[0];
		char[] password = (char[]) receivedObject[1];

		byte[] salt = new byte[32];
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String hashedPassword = HashingTools.hashPassword(password, salt);

		DatabaseControllerSecureCallable databaseControllerSecureCallable = new DatabaseControllerSecureCallable(new Message(message.getOpcode(), new String[] {username, hashedPassword, HashingTools.bytesToHex(salt)}), KEY, DBCkey);
		Message futureResult = databaseControllerSecureCallable.getFutureResult();
		if(futureResult.getObject() == null) {
			return null;
		}

		return (Player) futureResult.getObject();
	}

	public static Player authenticateUser(Message message) throws IOException {
		Object[] receivedObject = (Object[]) message.getObject();
		if(receivedObject == null) {
			return null;
		}
		String username = (String) receivedObject[0];
		char[] password = (char[]) receivedObject[1];

		Message coordinationUnitResponse = new CoordinationUnitCallable(new Message(1155, username)).getFutureResult();
		if(coordinationUnitResponse.getObject() != null)
			return null;

		DatabaseControllerSecureCallable databaseControllerSecureCallable = new DatabaseControllerSecureCallable(new Message(message.getOpcode(), username), KEY, DBCkey);
		Message futureResult = databaseControllerSecureCallable.getFutureResult();
		if(futureResult.getObject() == null) {
			return null;
		}

		Player player = (Player) ((Object[])futureResult.getObject())[0];

		String hashedPasswordInDB = ((String[])((Object[])futureResult.getObject())[1])[0];
		String saltInHex = ((String[])((Object[])futureResult.getObject())[1])[1];

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