package com.swarmer.shared.communication;

import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by Matt on 07-04-2017.
 */
public class MotherShipCallable implements Callable<Message> {

	public MotherShipCallable(Message message, ) {
		// TODO: Change to secure connection
		TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), )
	}

	@Override
	public Message call() throws Exception {
		return null;
	}
}
