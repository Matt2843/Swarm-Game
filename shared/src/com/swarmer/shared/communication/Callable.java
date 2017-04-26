package com.swarmer.shared.communication;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Callable {
	private Message futureResult = null;

	private Connection connection;

	public Callable(Connection connection, Message message) {
		this.connection = connection;
		try {

			connection.setProtocol(new Protocol() {
				@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
					futureResult = message;
				}
			});

			connection.start();
			System.out.println("Callable is sending");
			connection.sendMessage(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Message getFutureResult() {
		while(futureResult == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		connection.stopConnection();
		System.out.println("Callable received message and is terminating");
		return futureResult;
	}
}
