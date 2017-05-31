package com.swarmer.server.game;

import com.swarmer.server.game.logic.Game;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.units.GameUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.UDPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.UUID;

public class Swarmer implements Runnable {

	private Game game;
	private GameUnit gameUnit;
	private int offset;
	private String gameUUID;

	public Swarmer(HashMap<Player, LocationInformation> players, GameUnit gameUnit, int offset) throws InterruptedException, IOException {
		this.gameUnit = gameUnit;
		this.offset = offset;
		this.gameUUID = UUID.randomUUID().toString();

		UDPConnection udpConnection = connectToPlayers(players.size());

		game = new Game(players, 500, 500, udpConnection);
    }

	@Override public void run() {
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

		datagramSocket = new DatagramSocket(gameUnit.getPort() + offset + 4);

		udpConnection = new UDPConnection(datagramSocket, new GameProtocol(gameUnit));

		while(iterations < playerCount) {
			DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
			datagramSocket.receive(datagramPacket);
			udpConnection.addBroadcastAddress(datagramPacket.getSocketAddress());
			iterations++;
		}

		return udpConnection;
	}
}
