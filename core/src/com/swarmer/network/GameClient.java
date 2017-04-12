package com.swarmer.network;

import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;

public final class GameClient {
	
	private String host = "127.0.0.1";
	private int port = 1111;

	public static TCPConnection tcp = null;
	//public static UDPConnection udp;
	//public static SecureTCPConnection stcp;
	
	private static GameClient gc;

	private GameClient() {
		// DO NOT INSTANTIATE THIS CLASS
		establishTCPConnection(host, port);


		// TODO: FIX UDP + STCP
		//udp = new UDPConnection(new DatagramSocket(port), new ClientProtocol());
		//stcp = new SecureTCPConnection(new Socket(host, port), new ClientProtocol());
		//udp.start(); stcp.start();
	}

	public static GameClient getInstance() {
		if(gc == null) {
			//throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
			gc = new GameClient();
		}
		return gc;
	}

	public static void establishTCPConnection(String ip, int port) {
		if(tcp != null) tcp.stopConnection();
		try {
			tcp = new TCPConnection(new Socket(ip, port), new ClientProtocol());
			tcp.start();
			tcp.sendMessage(new Message(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void cleanUp() {
		tcp.cleanUp();
		//udp.cleanUp();
		//stcp.cleanUp();
	}
}
