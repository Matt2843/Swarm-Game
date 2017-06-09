package com.swarmer.shared.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class TCPConnection extends Connection {

	protected ObjectInputStream input;
	protected ObjectOutputStream output;

	protected Socket connection = null;

	private boolean stop = false;

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
				System.out.println("input.readObject() threw an IOException, cleaning up connection.");
				stop = true;
			} catch (ClassNotFoundException | InterruptedException | SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} while(!stop && message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		if(!stop) {
			output.writeObject(m);
			output.flush();
		}
	}

	@Override public Message getNextMsg() {
		try {
			return (Message) input.readObject();
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override protected void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}

	@Override public void stopConnection(Object... o) {
		if(!stop) {
			try {
				if(o.length > 0) {
					sendMessage(new Message(0, o[0]));
				} else {
					sendMessage(new Message(0));
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			stop = true;
			cleanUp();
		}
	}

	@Override public void cleanUp() {
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getConnection() {
		return connection;
	}
}
