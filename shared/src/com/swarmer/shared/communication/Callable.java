package com.swarmer.shared.communication;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Cap'n Odin on 17-04-2017.
 */
public class Callable {
	private Message futureResult = null;

	private TCPConnection tcpConnection;

	public Callable(Socket connection, Message message) {
		try {
			tcpConnection = new TCPConnection(connection, new Protocol() {
				@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
					futureResult = message;
				}
			});
			tcpConnection.start();
			System.out.println("Callable is sending");
			tcpConnection.sendMessage(message);
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
		tcpConnection.stopConnection();
		System.out.println("Callable received message and is terminating");
		return futureResult;
	}
}
