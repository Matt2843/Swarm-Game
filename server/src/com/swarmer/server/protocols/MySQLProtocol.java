package com.swarmer.server.protocols;

import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public class MySQLProtocol extends Protocol {
	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		this.caller = caller;
		switch (message.getOpcode()) {
			default:
				break;
		}
	}



}
