package com.swarmer.server;

import com.swarmer.shared.communication.*;
import com.swarmer.shared.communication.Connection;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by Matt on 07-04-2017.
 */
public class MotherShipCallable implements Callable<Message> {

	Message futureMessage = null;

	private class GenericProtocol extends Protocol {
		@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
			switch (message.getOpcode()) {
				case 999:
					futureMessage = message;
					break;
				default:
					break;
			}
		}
	}

	private GenericProtocol genericProtocol = new GenericProtocol();

	public MotherShipCallable(Message message) throws IOException {
		TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), genericProtocol);
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
		System.out.println("Hello world");
		return futureMessage;
	}
}
