package com.swarmer.server.protocols;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public class MySQLProtocol extends ServerProtocol {

	private Connection caller;

	public MySQLProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) {
		this.caller = caller;
		switch (message.getOpcode()) {
			default:
				break;
		}
	}

}
