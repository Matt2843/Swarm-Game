package com.swarmer.server;

import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public abstract class Unit {

	public static final int TCP = 0;
	public static final int STCP = 1;
	public static final int UDP = 2;

	public static final int DATABASE_CONTROLLER_TCP_PORT = 43100;
	//public static final int DATABASE_CONTROLLER_STCP_PORT = 43101;
	//public static final int DATABASE_CONTROLLER_UDP_PORT = 43102;

	public static final int COORDINATE_UNIT_TCP_PORT = 43110;
	//public static final int COORDINATE_UNIT_STCP_PORT = 43111;
	//public static final int COORDINATE_UNIT_UDP_PORT = 43112;

	public static final int ACCESS_UNIT_TCP_PORT = 43120;
	//public static final int ACCESS_UNIT_STCP_PORT = 43121;
	//public static final int ACCESS_UNIT_UDP_PORT = 43122;

	public static final int AUTHENTICATION_UNIT_TCP_PORT = 43130;
	//public static final int AUTHENTICATION_UNIT_STCP_PORT = 43131;
	//public static final int AUTHENTICATION_UNIT_UDP_PORT = 43132;

	public static final int LOBBY_UNIT_TCP_PORT = 43140;
	//public static final int LOBBY_UNIT_STCP_PORT = 43141;
	//public static final int LOBBY_UNIT_UDP_PORT = 43142;

	public static final int GAME_UNIT_TCP_PORT = 43150;
	//public static final int GAME_UNIT_STCP_PORT = 43151;
	//public static final int GAME_UNIT_UDP_PORT = 43152;

	public static KeyPair KEY;

	protected Unit() {
		generateKeys();
		startConnectionThreads();
	}

	protected void startConnectionThreads() {
		new ServerSocketThread(TCP).start();
		new ServerSocketThread(STCP).start();
		//new ServerSocketThread(UDP).start();
	}

	private KeyPair generateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KEY = kpg.generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public abstract int getPort();

	protected abstract ServerProtocol getProtocol();

	protected class ServerSocketThread extends Thread {

		private int serverSocketType;

		private ServerSocket serverSocket;
		private DatagramSocket datagramSocket;

		private Socket connection;

		public ServerSocketThread(int serverSocketType) {
			this.serverSocketType = serverSocketType;
		}

		private void awaitConnection() throws IOException {
			while(true) {
				if(serverSocketType == TCP) {
					connection = serverSocket.accept();
					new TCPConnection(connection, getProtocol()).start();
				} else if(serverSocketType == STCP) {
					connection = serverSocket.accept();
					new SecureTCPConnection(connection, getProtocol(), KEY, getProtocol().exPublicKey).start();
				} else if(serverSocketType == UDP) {
					// TODO: IMPLEMENT THIS VITAL CODE :)
				}
			}
		}

		@Override public void run() {
			try {
				if(serverSocketType == TCP || serverSocketType == STCP) {
					serverSocket = new ServerSocket(getPort() + serverSocketType);
				} else if(serverSocketType == UDP) {
					datagramSocket = new DatagramSocket(getPort() + serverSocketType);
				}
				awaitConnection();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
