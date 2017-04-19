package com.swarmer.server;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Callable;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 04/12/2017.
 */
public class DatabaseControllerCallable extends Callable {
	public DatabaseControllerCallable(Message message) throws IOException {
		super(new Socket("127.0.0.1", ServerUnit.DATABASE_CONTROLLER_TCP_PORT), message);
	}
}
