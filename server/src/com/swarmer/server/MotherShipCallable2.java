package com.swarmer.server;

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
public class MotherShipCallable2 {

	private Message futureResult = null;
	private boolean threadFlag = true;

	public MotherShipCallable2(Message message) {
		try {
			TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), new Protocol() {
				@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
					System.out.println(message.toString());
					switch (message.getOpcode()) {
						case 999:
							futureResult = message;
							
							break;
						default:
							break;
					}
				}
			});
			motherShipConnection.start();
			motherShipConnection.sendMessage(message);
			motherShipConnection.join();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Message getFutureResult() {
		return futureResult;
	}
}
