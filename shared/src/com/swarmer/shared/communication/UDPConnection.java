package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UDPConnection extends Connection {
	protected ObjectOutputStream output;
	protected ByteArrayOutputStream baos;

	protected ObjectInputStream input;
	protected ByteArrayInputStream iaos;

	protected DatagramPacket inbound;
	protected DatagramPacket outbound;

	protected byte[] buffer = new byte[512];

	protected DatagramSocket connection = null;

	private boolean stop = false;

	ArrayList<SocketAddress> broadcastAddress = new ArrayList<SocketAddress>();

	public UDPConnection(DatagramSocket connection, Protocol protocol) throws IOException {
		super(protocol);
		this.connection = connection;
		//correspondentsIp = connection.getRemoteSocketAddress().toString();
		inbound = new DatagramPacket(buffer, buffer.length);
		outbound = new DatagramPacket(new byte[512], 512);
		setupStreams();
	}

	public void changeAddress(int port) {
		connection.close();
		try {
			connection = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void addBroadcastAddress(SocketAddress ip) {
		broadcastAddress.add(ip);
		try {
			sendMessage(new Message(666, connection.getPort()), ip);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				connection.receive(inbound);
				message = (Message) new ObjectInputStream(new ByteArrayInputStream(inbound.getData())).readObject();
				System.out.println(message.toString());
				react(message);
			} catch (IOException e) {
				e.printStackTrace();
				stop = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} while(!stop && message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		output.writeObject(m);
		output.flush();
		outbound.setData(baos.toByteArray());
		System.out.println(outbound.getLength());
		for(SocketAddress inet : broadcastAddress) {
			outbound.setSocketAddress(inet);
			connection.send(outbound);
		}
	}

	public void sendMessage(Message m, SocketAddress inet) throws IOException {
		if(!stop) {
			output.writeObject(m);
			output.flush();
			outbound.setData(baos.toByteArray());
			System.out.println(outbound.getLength());
			outbound.setSocketAddress(inet);
			connection.send(outbound);
		}
	}

	@Override public Message getNextMsg() {
		return null;
	}

	@Override protected void setupStreams() throws IOException {
		baos = new ByteArrayOutputStream();
		output = new ObjectOutputStream(baos);
		output.flush();

		iaos = new ByteArrayInputStream(buffer);
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
			connection.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
