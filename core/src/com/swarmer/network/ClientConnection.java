package com.swarmer.network;

import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends SecureConnection {

	public ClientConnection(Socket connection) throws IOException {
		super(connection);
	}

	@Override protected void react(Message message) throws IOException, OperationInWrongServerNodeException {
		switch(message.getOpcode()) {
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
