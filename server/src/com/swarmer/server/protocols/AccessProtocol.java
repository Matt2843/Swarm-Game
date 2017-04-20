package com.swarmer.server.protocols;

import com.swarmer.server.units.AccessUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessProtocol extends ServerProtocol {

	private Connection caller;

	public AccessProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		this.caller = caller;
		System.out.println("Access unit protocol: " + message.toString());
		switch (message.getOpcode()) {
			case 1: // request best quality authentication_node from DB through mothership
				getAuthenticationUnit(new Message(message.getOpcode(), "authentication_units"));
				break;
			default:
				super.react(message, caller);
				break;
		}
	}

	private void getAuthenticationUnit(Message message) {
		try {
			Message sqlRespond = AccessUnit.getBestQualityAuthenticationNode(message);
			caller.sendMessage(sqlRespond);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
