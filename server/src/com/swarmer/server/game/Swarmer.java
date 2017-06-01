package com.swarmer.server.game;

import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.server.game.logic.Game;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.units.GameUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.UDPConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.UUID;

public class Swarmer implements Runnable {

	private Game game;
	private HashMap<Player, LocationInformation> players;
	private GameUnit gameUnit;
	private int port;
	private String gameUUID;

	public Swarmer(HashMap<Player, LocationInformation> players, GameUnit gameUnit, int port) throws InterruptedException, IOException {
		this.players = players;
		this.gameUnit = gameUnit;
		this.port = port;
		this.gameUUID = UUID.randomUUID().toString();

		game = new Game(players, 500, 500);
    }

	@Override public void run() {
		try {
			UDPConnection udpConnection = connectToPlayers(players.size());
			game.setUdpConnection(udpConnection);
		} catch (IOException ignored) {}

		while(true) {
			long time = System.currentTimeMillis();
			game.render();
			long sleep = 1000 - (System.currentTimeMillis() - time);
			if(sleep > 0) {
				try {
					Thread.sleep(sleep);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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

		while(iterations < playerCount) {
			DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
			datagramSocket.receive(datagramPacket);
			udpConnection.addBroadcastAddress(datagramPacket.getSocketAddress());
			iterations++;
		}
		System.out.println("UDP Setup Completed");

		return udpConnection;
	}

	public Graph getMap() {
		return game.getGraph();
	}
}
