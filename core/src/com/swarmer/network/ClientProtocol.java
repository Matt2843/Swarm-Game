package com.swarmer.network;

import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.*;

import java.io.IOException;

/**
 * Created by Matt on 04/06/2017.
 */
public class ClientProtocol {



	public void react(Message message) {
		switch (message.getOpcode()) {
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
				System.out.println("Hello hello" + message.getObject().toString());
				String[] receivedMessageArray = (String[]) message.getObject();
				LobbyScreen.lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
				break;
			default:
				break;
		}
	}

}
