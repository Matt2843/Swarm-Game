package com.swarmer.server.protocols;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 19-04-2017.
 */
public abstract class ServerProtocol extends Protocol {

	private ServerUnit serverUnit;

	protected ServerProtocol(ServerUnit serverUnit) {
		this.serverUnit = serverUnit;
	}

	@Override protected abstract void react(Message message, Connection caller) throws IOException, SQLException;

	public void addConnectionToActiveConnections(Player player, Connection connection) {
		serverUnit.addActiveConnection(player, connection);
	}

}

