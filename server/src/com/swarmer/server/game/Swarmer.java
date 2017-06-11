package com.swarmer.server.game;

import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.server.game.logic.Game;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.units.GameUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.UDPConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class Swarmer extends Thread {

	public Game game;
	private HashMap<Player, Connection> players;
	private GameUnit gameUnit;
	private int port;
	private String gameUUID;

	public Swarmer(HashMap<Player, Connection> players, GameUnit gameUnit, int port) throws InterruptedException, IOException {
		this.players = players;
		this.gameUnit = gameUnit;
		this.port = port;
		this.gameUUID = UUID.randomUUID().toString();

		game = new Game(players, 100, 100);
    }

	@Override public void run() {
		/*try {
			UDPConnection udpConnection = connectToPlayers(players.size());
			game.setUdpConnection(udpConnection);
		} catch (IOException ignored) {}*/

		boolean bool = true;

		while(bool) {
			long time = System.currentTimeMillis();
			bool = game.render();
			long sleep = 500 - (System.currentTimeMillis() - time);
			if(sleep > 0) {
				try {
					Thread.sleep(sleep);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Game Terminated");
	}

    public String getGameUUID() {
        return gameUUID;
    }

	private UDPConnection connectToPlayers(int playerCount) throws IOException {
		DatagramSocket datagramSocket;
		UDPConnection udpConnection;
		int iterations = 0;

		datagramSocket = new DatagramSocket(port);

		udpConnection = new UDPConnection(datagramSocket, new GameProtocol(gameUnit));

//		while(iterations < playerCount) {
//			DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
//			datagramSocket.receive(datagramPacket);
//			udpConnection.addBroadcastAddress(new InetSocketAddress("localhost", 4445));
//			iterations++;
//		}

			DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
			datagramSocket.receive(datagramPacket);
			udpConnection.addBroadcastAddress(new InetSocketAddress("localhost", 4445));

			datagramPacket = new DatagramPacket(new byte[512], 512);
			datagramSocket.receive(datagramPacket);
			udpConnection.addBroadcastAddress(new InetSocketAddress("localhost", 4449));

		System.out.println("UDP Setup Completed");

		return udpConnection;
	}

	public void spawnAnt(Player owner) {
		game.spawnAnt(owner);
	}

	public Graph getMap() {
		return game.getGraph();
	}
}
