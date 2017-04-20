package com.swarmer.network;

import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ClientProtocol extends Protocol {

	@Override public void react(Message message, Connection caller) {
		System.out.println(message.toString());
		switch (message.getOpcode()) {
			case 1: // TEST
				System.out.println(message.toString());
				break;
			case 110: // Login succeeded
				if (message.getObject() != null) {
					ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
					GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
				} else {
					// TODO: Notify user that login failed.
					System.out.println("Login failed");
				}
				break;
			case 202: // User creation state
				if(message.getObject() != null) {
					ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
					GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
				} else {
					// TODO: Notify user that user creation failed.
					System.out.println();
				}
				break;
			case 304: // Received message in lobby chat
				String[] receivedMessageArray = (String[]) message.getObject();
				LobbyScreen.lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
				break;
			case 999:
				connectToAuthNode(message);
				break;
			default:
				break;
		}
	}

	private void connectToAuthNode(Message message) {
		String[] receivedMessageArray = ((String) message.getObject()).split(":");
		String ip = receivedMessageArray[0].replace("/", "");
		int port = Integer.parseInt(receivedMessageArray[1]);
		GameClient.getInstance().establishTCPConnection(ip, port);
		//GameClient.getInstance().establishSecureTCPConnection(ip, port);
	}
}
