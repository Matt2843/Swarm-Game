package com.swarmer.server;

import com.swarmer.shared.communication.Callable;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/12/2017.
 */
public class MotherShipCallable2 extends Callable {
	public MotherShipCallable2(Message message) throws IOException {
		super(new Socket("127.0.0.1", 1110), message);
	}
}
