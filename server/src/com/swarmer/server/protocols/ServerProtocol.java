package com.swarmer.server.protocols;

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
	private Connection caller;

	protected ServerProtocol(ServerUnit serverUnit) {
		this.serverUnit = serverUnit;
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException, InterruptedException {
		this.caller = caller;
		switch (message.getOpcode()) {
			case 0:
				removeConnectionFromActiveConnections(message, caller);
				break;
			case 1:
				addConnectionToActiveConnections(message, caller);
				break;
			case 888:
				forwardMessage(message);
				break;
			case 890: // Invite user to lobby.
				inviteFriendToLobby(message);
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
	private void sharePublicKeys(Message message) throws IOException {
		if(exPublicKey != message.getObject()) {
			exPublicKey = (PublicKey) message.getObject();
			caller.sendMessage(new Message(11111, serverUnit.KEY.getPublic()));
		}
	}

	private void inviteFriendToLobby(Message message) throws IOException {
		serverUnit.addFriendToLobby(message);
	}


	private void forwardMessage(Message message) throws IOException {
		serverUnit.forwardMessage(message);
	}

	private void sharePublicKey(Message message, Connection caller) throws IOException {
		if(exPublicKey != (PublicKey) message.getObject()) {
			exPublicKey = (PublicKey) message.getObject();
			caller.sendMessage(new Message(11111, AuthenticationUnit.KEY.getPublic()));
		}
	}

	private void addFriendShip(Message message) throws IOException {
		serverUnit.addFriendShip(message);
	}

	private void sendFriendRequest(Message message) throws IOException {
		// Convert message.getObject[0] i.e. from username, message.getObject[1] i.e. to username to Players.
		serverUnit.sendFriendRequest(message);
	}

	private boolean addConnectionToActiveConnections(Message message, Connection connection) throws IOException {
		return addConnectionToActiveConnections((Player) message.getObject(), connection);
	}

	protected boolean addConnectionToActiveConnections(Player player, Connection connection) throws IOException {
		connection.setPlayer(player);
		return serverUnit.addActiveConnection(player, connection);
	}

	private boolean removeConnectionFromActiveConnections(Message message, Connection connection) throws IOException {
		Player player = (Player) message.getObject();
		if(player == null) {
			player = connection.getPlayer(); // ToDO test
		}
		return serverUnit.removeActiveConnection(player);
	}
}

