package com.swarmer.server.protocols;

import com.swarmer.server.MotherShip;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public class MotherShipProtocol extends Protocol {

	@Override protected void react(Message message, Connection caller) {
		switch (message.getOpcode()) {
			case 1:
				try {
					caller.sendMessage(new Message(2, MotherShip.mySQLConnection.sqlExecuteQueryToString("SELECT * FROM authentication_nodes ORDER BY user_count ASC LIMIT 1")));
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}
}
