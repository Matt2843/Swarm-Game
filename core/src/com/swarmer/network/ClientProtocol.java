package com.swarmer.network;

import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ClientProtocol extends Protocol {

	@Override
	protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {
		switch (message.getOpcode()) {
			case 1: // TEST
				System.out.println(message.toString());
				break;
			case 110: // Login succeeded
				ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
				break;
			case 111: // Login failed
				break;
			case 202: // User created
				ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
				break;
			case 203: // User creation failed
				break;
			case 304: // Received message in lobby chat
				String[] receivedMessageArray = (String[]) message.getObject();
				LobbyScreen.lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
				break;
			default:
				break;
		}

	}
}
