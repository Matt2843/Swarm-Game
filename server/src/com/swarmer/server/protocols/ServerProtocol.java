package com.swarmer.server.protocols;

import com.swarmer.server.CoordinationUnitCallable;
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

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		switch (message.getOpcode()) {
			case 0:
				break;
			case 1:
				break;
			default:
				break;
		}
	};

	public boolean addConnectionToActiveConnections(Player player, Connection connection) throws IOException {
		serverUnit.addActiveConnection(player, connection);
		return (boolean) new CoordinationUnitCallable(new Message(1150, new Object[]{player, serverUnit.getDescription(), serverUnit.getPort()})).getFutureResult().getObject();
	}

	public boolean removeConnectionFromActiveConnections(Player player) throws IOException {
		serverUnit.removeActiveConnection(player);
		return (boolean) new CoordinationUnitCallable(new Message(1152, player)).getFutureResult().getObject();
	}

}

