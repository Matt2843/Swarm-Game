package com.swarmer.server;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Callable;
import com.swarmer.shared.communication.IPGetter;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.SecureTCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

public class DatabaseControllerSecureCallable extends Callable {
	public DatabaseControllerSecureCallable(Message message, KeyPair key, PublicKey exPublicKey) throws IOException {
		super(new SecureTCPConnection(new Socket(IPGetter.getInstance().getDatabaseControllerIP(), ServerUnit.DATABASE_CONTROLLER_TCP_PORT + 1), null, key, exPublicKey), message);
	}
}