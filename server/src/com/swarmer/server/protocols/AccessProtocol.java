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
public class AccessProtocol extends Protocol {

	@Override protected void react(Message message, Connection caller) throws IOException {
		switch (message.getOpcode()) {
			case 1: // request best quality authentication_node from DB through mothership


				try {
					caller.sendMessage(new Message(2, MotherShip.mySQLConnection.sqlExecuteQueryToString("SELECT * FROM authentication_nodes ORDER BY user_count ASC LIMIT 1")));
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 2: // mothership found authentication_node and returned it in .Object();
				break;
			case 3: // mothership did not handle the fetch authentication_node request.
				break;
			default:
				break;
		}

	}
}
