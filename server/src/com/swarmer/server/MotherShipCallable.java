package com.swarmer.server;

import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by Matt on 07-04-2017.
 */
public class MotherShipCallable implements Callable<Message> {

	private Protocol protocol;

	public MotherShipCallable(Message message, Protocol protocol) throws IOException {
		this.protocol = protocol;
		TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), protocol);
		motherShipConnection.start();
		motherShipConnection.sendMessage(message);
		try {
			motherShipConnection.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Message call() throws Exception {
		return protocol.getFutureMessage();
	}
}
