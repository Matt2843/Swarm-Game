package com.swarmer.network;

import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ClientProtocol extends Protocol{

	@Override
	protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
		System.out.println(message.toString());
		switch (message.getOpcode()) {
			case 1: // TEST
				System.out.println(message.toString());
				break;
			case 110: // Login succeeded
				if((boolean) message.getObject() == true)
					ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
				else {
					// TODO: Notify user that login failed.
				}
				break;
			case 202: // User creation state
				if((boolean) message.getObject() == true)
					ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
				else {
					// TODO: Notify user that user creation failed.
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
	}
}
