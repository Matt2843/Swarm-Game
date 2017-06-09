package com.swarmer.shared.communication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class Connection extends Thread {

	protected String correspondentsIp = "";
	protected Player player = null;

	protected Protocol protocol;

	protected Connection(Protocol protocol) throws IOException {
		this.protocol = protocol;
	}

	abstract public void run();

	protected void react(Message message) throws IOException, SQLException, NoSuchAlgorithmException, InterruptedException {
		protocol.react(message, this);
	}

	abstract public void sendMessage(Message m) throws IOException;

	abstract public Message getNextMsg();

	abstract protected void setupStreams() throws IOException;

	abstract protected void stopConnection(Object... o);

	abstract protected void cleanUp();

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
