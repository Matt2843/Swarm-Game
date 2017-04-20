package com.swarmer.network;

import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public final class GameClient {
	
	private String host = "127.0.0.1";
	private int port = 43120;

	private static final ClientProtocol clientProtocol = new ClientProtocol();

	public static TCPConnection tcp = null;
	public static SecureTCPConnection stcp = null;
	//public static UDPConnection udp;

	public static KeyPair KEY = null;

	private static GameClient gc;

	private GameClient() {
		// DO NOT INSTANTIATE THIS CLASS

		try {
			KEY = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		establishTCPConnection(host, port);

		try {
			tcp.sendMessage(new Message(1));
		} catch(IOException e) {
			e.printStackTrace();
		}

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

	public static TCPConnection establishTCPConnection(String ip, int port) {
		try {
			if(tcp != null) {
				tcp.stopConnection();
			}
			tcp = new TCPConnection(new Socket(ip, port), clientProtocol);
			tcp.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tcp;
	}

	public static void establishSecureTCPConnection(String ip, int port, PublicKey exPublicKey) {
		try {
			if(stcp != null) {
				stcp.stopConnection();
			}
			stcp = new SecureTCPConnection(new Socket(ip, port + 1), clientProtocol, KEY, exPublicKey);
			stcp.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void cleanUp() {
		tcp.cleanUp();
		stcp.cleanUp();
		//udp.cleanUp();
	}
}
