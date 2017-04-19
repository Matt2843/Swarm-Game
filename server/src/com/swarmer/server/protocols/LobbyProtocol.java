package com.swarmer.server.protocols;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;

/**
 * Created by Matt on 04/06/2017.
 */
public class LobbyProtocol extends ServerProtocol {

	public LobbyProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) {

	}
}
