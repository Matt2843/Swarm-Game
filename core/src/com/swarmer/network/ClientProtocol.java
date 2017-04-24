package com.swarmer.network;

import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.PublicKey;

public class ClientProtocol extends Protocol {

	private String ip;
	private int port;

	@Override public void react(Message message, Connection caller) throws IOException {
		System.out.println(message.toString());
		switch (message.getOpcode()) {
			case 1: // TEST
				break;
			case 110: // Login succeeded
				loginSucceeded(message);
				break;
			case 202: // User creation state
				userCreatingState(message);
				break;
			case 301: // Received message in lobby chat
				String[] receivedMessageArray = (String[]) message.getObject();
				LobbyScreen.getInstance().lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
				break;
			case 997:
				connectedToLobby(message);
				break;
			case 998:
				connectToLobbyUnitAndStartLobby(message);
				break;
			case 999:
				connectServerUnit(message);
				break;
			case 11111:
				secureConnectToAuthNode(message);
				break;
			default:
				break;
		}
	}

	private void userCreatingState(Message message) {
		if(message.getObject() != null) {
			SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());
			//ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
			GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
		} else {
			// TODO: Notify user that user creation failed.
			System.out.println();
		}
	}

	private void loginSucceeded(Message message) {
		if (message.getObject() != null) {
			SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());
			//ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
			GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
		} else {
			// TODO: Notify user that login failed.
			System.out.println("Login failed");
		}
	}

	private void connectedToLobby(Message message) {
		LobbyScreen.getInstance().setLobbyId((String) message.getObject());
		SwarmerMain.getInstance().show(LobbyScreen.getInstance());
	}

	private void connectToLobbyUnitAndStartLobby(Message message) throws IOException {
		connectServerUnit(message);
		GameClient.getInstance().tcp.sendMessage(new Message(302));
	}

	private void connectServerUnit(Message message) {
		String[] receivedMessageArray = ((String) message.getObject()).split(":");
		ip = receivedMessageArray[0].replace("/", "");
		port = Integer.parseInt(receivedMessageArray[1]);
		try {
			GameClient.getInstance().establishTCPConnection(ip, port).sendMessage(new Message(11111, GameClient.KEY.getPublic()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void secureConnectToAuthNode(Message message) {
		GameClient.getInstance().establishSecureTCPConnection(ip, port, (PublicKey) message.getObject());
	}
}
