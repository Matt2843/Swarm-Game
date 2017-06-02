package com.swarmer.network;

import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public final class GameClient {

	private static final ClientProtocol clientProtocol = new ClientProtocol();

	public TCPConnection tcp = null;
	public SecureTCPConnection stcp = null;
	public UDPConnection udp;

	private static Player currentPlayer;

	public static KeyPair KEY = null;

	private static GameClient gc;

	public static String currentGame;

	private GameClient() {
		// DO NOT INSTANTIATE THIS CLASS

		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KEY = kpg.generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String IP = IPGetter.getInstance().getAccessUnitIP();
		establishTCPConnection(IP, 43120);

		// TODO: FIX UDP
		try {
			udp = new UDPConnection(new DatagramSocket(4449), new ClientProtocol());
		} catch (IOException e) {
			e.printStackTrace();
		}
		udp.start();
	}

	public static GameClient getInstance() {
		if(gc == null) {
			//throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
			gc = new GameClient();
		}
		return gc;
	}

	public void establishTCPConnection(String ip, int port) {
		closeConnection();
		try {
			tcp = new TCPConnection(new Socket(ip, port), clientProtocol);
			tcp.start();
			tcp.sendMessage(new Message(1, currentPlayer));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void establishSecureTCPConnection(String ip, int port, PublicKey exPublicKey) {
		try {
			if(stcp != null) {
				stcp.stopConnection(currentPlayer);
			}
			stcp = new SecureTCPConnection(new Socket(ip, port + 1), clientProtocol, KEY, exPublicKey);
			stcp.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		if(tcp != null) {
			tcp.stopConnection(currentPlayer);
		}

		if(stcp != null) {
			stcp.stopConnection(currentPlayer);
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	public void cleanUp() {
		tcp.cleanUp();
		stcp.cleanUp();
		udp.cleanUp();
	}
}
