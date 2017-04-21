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
import java.security.PublicKey;
import java.sql.SQLException;

public class ClientProtocol extends Protocol {

	private String ip;
	private int port;

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
			case 11111:
				secureConnectToAuthNode(message);
				break;
			default:
				break;
		}
	}

	private void connectToAuthNode(Message message) {
		String[] receivedMessageArray = ((String) message.getObject()).split(":");
		ip = receivedMessageArray[0].replace("/", "");
		port = Integer.parseInt(receivedMessageArray[1]);

		try {
			GameClient.getInstance().establishTCPConnection(ip, port);
			GameClient.tcp.sendMessage(new Message(11111, GameClient.KEY.getPublic()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void secureConnectToAuthNode(Message message) {
		GameClient.getInstance().establishSecureTCPConnection(ip, port, (PublicKey) message.getObject());
		try {
			GameClient.stcp.sendMessage(new Message(1111, GameClient.KEY.getPublic()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
