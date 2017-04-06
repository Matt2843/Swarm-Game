package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class UDPConnection extends Connection {
	protected ObjectOutputStream output;
	protected ByteArrayOutputStream baos;

	protected ObjectInputStream input;
	protected ByteArrayInputStream iaos;

	protected DatagramPacket inbound;
	protected DatagramPacket outbound;

	protected byte[] buffer = new byte[64];

	protected DatagramSocket connection = null;

	public UDPConnection(DatagramSocket connection) throws IOException {
		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		inbound = new DatagramPacket(buffer, buffer.length);
		outbound = new DatagramPacket(new byte[64], 64);
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
			}
		} while(message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
		outbound.setData(baos.toByteArray());
		connection.send(outbound);
	}

	@Override protected void setupStreams() throws IOException {
		baos = new ByteArrayOutputStream();
		output = new ObjectOutputStream(baos);
		output.flush();

		iaos = new ByteArrayInputStream(buffer);
		input = new ObjectInputStream(iaos);
	}

	@Override protected void cleanUp() {
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
