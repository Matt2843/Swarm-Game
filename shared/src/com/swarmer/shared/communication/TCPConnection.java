package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPConnection extends Connection {

	protected ObjectInputStream input;
	protected ObjectOutputStream output;

	protected Socket connection = null;

	public TCPConnection(Socket connection, Protocol protocol) throws IOException {
		super(protocol);
		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		setupStreams();
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				message = (Message) input.readObject();
				react(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			}
		} while(message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
	}

	@Override protected void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}

	@Override protected void cleanUp() {
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
