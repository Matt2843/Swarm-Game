package com.swarmer.server.protocols;

import com.swarmer.server.units.CoordinationUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationProtocol extends Protocol {

	private Connection caller = null;

	private Player connectedPlayer;
	private String connectedPlayerUnitDescription;
	private String connectedPlayerCurrentUnitIp;
	private int connectedPlayerCurrentUnitPort;


	@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
		this.caller = caller;
		System.out.println("Coordination Protocol: " + message.toString());
		switch(message.getOpcode()) {
			case 1150: // User connected to a server unit, object: {Player, Unit_Description, Unit_IP, Unit_Port}
				addPlayer(message);
				break;
			case 1151: // User changed server unit, object: {Player, Unit_Description, Unit_IP, Unit_Port}
				changePlayerLocationInformation(message);
				break;
			case 1152: // User disconnected completely from the server, object: {Player}
				removePlayer(message);
				break;
			case 1153: // Retrieve user location information with the given username, object: {username}
				findPlayer(message);
				break;
			case 1161:
				findMatch(message);
			default:
				break;
		}
	}

	private void findMatch(Message message) {
		ArrayList<Player> players = (ArrayList<Player>) message.getObject();

		CoordinationUnit.findMatch(players);
	}

	private void addPlayer(Message message) {
		CoordinationUnit.addConnection(connectedPlayer, generateLocationInformation(message));
	}

	private void changePlayerLocationInformation(Message message) {
		CoordinationUnit.changeLocationInformation(connectedPlayer, generateLocationInformation(message));
	}

	private void removePlayer(Message message) {
		Player player = (Player) message.getObject();
		CoordinationUnit.removeConnection(player);
	}

	private void findPlayer(Message message) throws IOException {
		String searchString = (String) message.getObject();
		LocationInformation locationInformation = CoordinationUnit.findPlayerLocationInformation(searchString);
		caller.sendMessage(new Message(1248732, locationInformation)); // TODO: IMPLEMENT THIS IN THE GAME CLIENT.
	}

	private LocationInformation generateLocationInformation(Message message) {
		Object[] receivedObjects = (Object[]) message.getObject();
		Player connectedPlayer = (Player) receivedObjects[0];
		String connectedPlayerUnitDescription = (String) receivedObjects[1];
		String connectedPlayerCurrentUnitIp = (String) receivedObjects[2];
		int connectedPlayerCurrentUnitPort = (int) receivedObjects[3];

		this.connectedPlayer = connectedPlayer;
		this.connectedPlayerUnitDescription = connectedPlayerUnitDescription;
		this.connectedPlayerCurrentUnitIp = connectedPlayerCurrentUnitIp;
		this.connectedPlayerCurrentUnitPort = connectedPlayerCurrentUnitPort;
		return new LocationInformation(connectedPlayerUnitDescription, connectedPlayerCurrentUnitIp, connectedPlayerCurrentUnitPort);
	}
}
