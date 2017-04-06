package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;

public abstract class Connection implements Runnable {

	protected String correspondentsIp = "";
	protected Player player = null;

	abstract public void run();

	protected void react(Message message) throws IOException, OperationInWrongServerNodeException {
		//Protocol.getInstance().react(message);
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
