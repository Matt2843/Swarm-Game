package com.swarmer.shared.communication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public abstract class Protocol {

	protected Message futureMessage = null;

	protected abstract void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException;

	public Message getFutureMessage() {
		while(futureMessage == null) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return futureMessage;
	}

}
