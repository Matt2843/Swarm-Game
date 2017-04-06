package com.swarmer.network;

import com.badlogic.gdx.Game;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.GameClientNotInstantiatedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public final class GameClient extends Thread {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private String host = "localhost";
	private int port = 1111;
	
	private Socket client;

	private static GameClient gc;

	private GameClient() {
		// DO NOT INSTANTIATE THIS CLASS
	}

	public static void initializeGameClient() {
		gc = new GameClient();
		gc.start();
	}

	public static GameClient getInstance() throws GameClientNotInstantiatedException {
		if(gc == null) {
			throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
		}
		return gc;
	}
	
	@Override public void run() {
		try {
			connectToService();
			setupStreams();
			whileConnected();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			cleanUp();
		}
	}
	
	public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
	}
	
	private void whileConnected() throws IOException, ClassNotFoundException {
		Message message = null;
		do {
			message = (Message) input.readObject();
			System.out.println("Message Received + " + message.getOpcode());
			react(message);
		} while(message.getOpcode() != 0);
		cleanUp();
	}

	private void react(Message message) {
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
				System.out.println("Hello hello" + message.getObject().toString());
				String[] receivedMessageArray = (String[]) message.getObject();
				LobbyScreen.lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
				break;
			default:
				break;
		}
	}

	private void connectToService() throws IOException {
		client = new Socket(InetAddress.getByName(host), port);
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
	}

	public void cleanUp() {
		try {
			output.close();
			input.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
