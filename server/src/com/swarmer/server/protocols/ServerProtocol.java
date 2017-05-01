package com.swarmer.server.protocols;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.SQLException;

public abstract class ServerProtocol extends Protocol {

	protected ServerUnit serverUnit;

	protected ServerProtocol(ServerUnit serverUnit) {
		this.serverUnit = serverUnit;
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		switch (message.getOpcode()) {
			case 0:
				removeConnectionFromActiveConnections((Player) message.getObject());
				break;
			case 1:
				addConnectionToActiveConnections((Player) message.getObject(), caller);
				break;
			case 11111:
				sharePublicKey(message, caller);
				break;
			case 34789: // Friend Request, String[] {String From, String To}
				sendFriendRequest(message);
				break;
			case 34788: // Friend Request accepted String[] {String user1, String user2}
				addFriendShip(message);
				break;
			default:
				break;
		}
	}

	private void sharePublicKey(Message message, Connection caller) throws IOException {
		if(exPublicKey != (PublicKey) message.getObject()) {
			exPublicKey = (PublicKey) message.getObject();
			caller.sendMessage(new Message(11111, AuthenticationUnit.KEY.getPublic()));
		}
	}

	private void addFriendShip(Message message) {
		serverUnit.addFriendShip(((String[])message.getObject())[0], ((String[])message.getObject())[1]);
	}

	private void sendFriendRequest(Message message) throws IOException {
		serverUnit.sendFriendRequest(((String[])message.getObject())[0], ((String[])message.getObject())[1]);
	}

	private boolean addConnectionToActiveConnections(Player player, Connection connection) throws IOException {
		connection.setPlayer(player);
		serverUnit.addActiveConnection(player, connection);
		return (boolean) new CoordinationUnitCallable(new Message(1150, new Object[]{player, serverUnit.getDescription(), serverUnit.getPort()})).getFutureResult().getObject();
	}

	private boolean removeConnectionFromActiveConnections(Player player) throws IOException {
		serverUnit.removeActiveConnection(player);
		return (boolean) new CoordinationUnitCallable(new Message(1152, player)).getFutureResult().getObject();
	}

}

