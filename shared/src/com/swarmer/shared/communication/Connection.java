package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;

public abstract class Connection implements Runnable {

	protected String correspondentsIp = "";
	protected Player player = null;

	protected Protocol protocol;

	protected Connection(Protocol protocol) throws IOException {
		this.protocol = protocol;
	}

	abstract public void run();

	protected void react(Message message) throws IOException, OperationInWrongServerNodeException {
		protocol.react(message, this);
	}

	abstract public void sendMessage(Message m) throws IOException;

	abstract protected void setupStreams() throws IOException;

	abstract protected void cleanUp();
	
	public String getCorrespondentsIp() {
		return correspondentsIp;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
