package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UDPConnection extends Connection {
	protected ObjectOutputStream output;
	protected ByteArrayOutputStream baos;

	protected ObjectInputStream input;
	protected ByteArrayInputStream iaos;

	protected DatagramPacket inbound;
	protected DatagramPacket outbound;

	protected byte[] buffer = new byte[512];

	protected DatagramSocket connection = null;

	public UDPConnection(DatagramSocket connection, Protocol protocol) throws IOException {
		super(protocol);
		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		inbound = new DatagramPacket(buffer, buffer.length);
		outbound = new DatagramPacket(new byte[512], 512);
		setupStreams();
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				connection.receive(inbound);
				message = (Message) input.readObject();
				react(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} while(message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
		outbound.setData(baos.toByteArray());
		System.out.println(outbound.getLength());
		connection.send(outbound);
	}

	@Override public Message getNextMsg() {
		return null;
	}

	@Override protected void setupStreams() throws IOException {
		baos = new ByteArrayOutputStream();
		output = new ObjectOutputStream(baos);
		output.flush();

		iaos = new ByteArrayInputStream(buffer);
		input = new ObjectInputStream(iaos);
	}

	@Override public void stopConnection(Object... o) {
		try {
			if(o.length > 0) {
				sendMessage(new Message(0, o[0]));
			} else {
				sendMessage(new Message(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		cleanUp();
	}

	@Override public void cleanUp() {
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
