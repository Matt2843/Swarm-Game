package com.swarmer.network;

import com.badlogic.gdx.Game;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.GameClientNotInstantiatedException;

import java.net.InetAddress;
import java.net.Socket;

public final class GameClient {
	
	private String host = "127.0.0.1";
	private int port = 1111;

	private static TCPConnection tcp;
	private static UDPConnection udp;
	private static SecureTCPConnection stcp;
	
	private static GameClient gc;

	private GameClient() {
		// DO NOT INSTANTIATE THIS CLASS
		tcp = new TCPConnection(new Socket(host, port), protocol);
		udp = new UDPConnection(new DatagramSocket(host, port), protocol);
		stcp = new SecureTCPConnection(new Socket(host, port), protocol);
		tcp.start(); udp.start(); stcp.start();
	}

	public static GameClient getInstance() {
		if(gc == null) {
			//throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
			gc = new GameClient();
		}
		return gc;
	}
}
