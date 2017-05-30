package com.swarmer.server.protocols;

import com.swarmer.server.units.CoordinationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationProtocol extends ServerProtocol {

	private Connection caller = null;

	private Player connectedPlayer;
	private String connectedPlayerUnitDescription;
	private String connectedPlayerCurrentUnitIp;
	private int connectedPlayerCurrentUnitPort;

	public CoordinationProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}


	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		this.caller = caller;
		System.out.println("Coordination Protocol: " + message.toString());
		switch(message.getOpcode()) {
			case 1150: // User connected to a server unit, object: {Player, Unit_Description, Unit_Port}
				addPlayer(message);
				break;
			case 1151: // User changed server unit, object: {Player, Unit_Description, Unit_Port}
				changePlayerLocationInformation(message);
				break;
			case 1152: // User disconnected completely from the server, object: {Player}
				removePlayer(message);
				break;
			case 1153: // Retrieve user location information with the given username, object: {username}
				findPlayer(message, caller);
				break;
			case 1154: // Retrieve player profile from a username.
				findPlayerReturnPlayer(message);
				break;
			case 1161:
			case 13371:
				findMatch(message);
			default:
				break;
		}
	}

	private void findPlayerReturnPlayer(Message message) throws IOException {
		Player target = CoordinationUnit.findPlayerReturnPlayer(message);
		caller.sendMessage(new Message(target));
	}

	private void findMatch(Message message) throws IOException {
		caller.sendMessage(new Message(13371, true));
		ArrayList<Player> players = (ArrayList<Player>) message.getObject();
		CoordinationUnit.findMatch(players);
	}

	private void addPlayer(Message message) throws IOException {
		LocationInformation locationInformation = generateLocationInformation(message);
		CoordinationUnit.addConnection(connectedPlayer, locationInformation);
		caller.sendMessage(new Message(123342, true));
	}

	private void changePlayerLocationInformation(Message message) {
		LocationInformation locationInformation = generateLocationInformation(message);
		CoordinationUnit.changeLocationInformation(connectedPlayer, locationInformation);
	}

	private void removePlayer(Message message) throws IOException {
		Player player = (Player) ((Object[])message.getObject())[0];
		String serverId = (String) ((Object[])message.getObject())[1];

		CoordinationUnit.removeConnection(player, serverId);
		caller.sendMessage(new Message(1236324876, true));
	}

	private void findPlayer(Message message, Connection caller) throws IOException {
		String searchString = (String) message.getObject();
		LocationInformation locationInformation = CoordinationUnit.findPlayerLocationInformation(searchString);
		caller.sendMessage(new Message(1248732, locationInformation)); // TODO: IMPLEMENT THIS IN THE GAME CLIENT.
	}

	private LocationInformation generateLocationInformation(Message message) {
		Object[] receivedObjects = (Object[]) message.getObject();
		Player connectedPlayer = (Player) receivedObjects[0];
		String connectedPlayerUnitDescription = (String) receivedObjects[1];
		int connectedPlayerCurrentUnitPort = (int) receivedObjects[2];

		this.connectedPlayer = connectedPlayer;
		this.connectedPlayerUnitDescription = connectedPlayerUnitDescription;
		this.connectedPlayerCurrentUnitIp = ((TCPConnection) caller).getConnection().getInetAddress().toString();
		this.connectedPlayerCurrentUnitPort = connectedPlayerCurrentUnitPort;
		return new LocationInformation(connectedPlayerUnitDescription, connectedPlayerCurrentUnitIp, connectedPlayerCurrentUnitPort);
	}
}
