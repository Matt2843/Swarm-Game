package com.swarmer.network;

import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;
import com.swarmer.shared.communication.UDPConnection;

import java.io.IOException;
import java.net.Socket;

public final class GameClient {
	
	private String host = "127.0.0.1";
	private int port = 1111;

	public static TCPConnection tcp;
	//public static UDPConnection udp;
	//public static SecureTCPConnection stcp;
	
	private static GameClient gc;

	private GameClient() throws IOException {
		// DO NOT INSTANTIATE THIS CLASS
		tcp = new TCPConnection(new Socket(host, port), new ClientProtocol());
		//udp = new UDPConnection(new DatagramSocket(port), new ClientProtocol());
		//stcp = new SecureTCPConnection(new Socket(host, port), new ClientProtocol());
		tcp.start(); //udp.start(); stcp.start();
		tcp.sendMessage(new Message(1));
	}

	public static GameClient getInstance() throws IOException {
		if(gc == null) {
			//throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
			gc = new GameClient();
		}
		return gc;
	}

	public static void cleanUp() {
		tcp.cleanUp();
		//udp.cleanUp();
		//stcp.cleanUp();
	}
}
