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

	public int size = 512;

	protected byte[] inbuffer;
	protected byte[] outbuffer;

	protected DatagramSocket connection = null;

	private boolean stop = false;

	ArrayList<SocketAddress> broadcastAddress = new ArrayList<SocketAddress>();

	public UDPConnection(DatagramSocket connection, Protocol protocol) throws IOException {
		super(protocol);
		inbuffer = new byte[size];
		outbuffer = new byte[size];
		this.connection = connection;
		connection.setBroadcast(false);
		inbound = new DatagramPacket(inbuffer, inbuffer.length);
		outbound = new DatagramPacket(outbuffer, outbuffer.length);
		setupStreams();
	}

	public void addBroadcastAddress(SocketAddress ip) {
		broadcastAddress.add(ip);
		try {
			System.out.println(connection.getBroadcast());
			sendMessage(new Message(666, new InetSocketAddress("127.0.0.1", connection.getLocalPort())), ip);
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
				react(message);
			} catch (IOException e) {
				e.printStackTrace();
				//stop = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(!stop && message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		if(!stop) {
			setupStreams();
			output.writeObject(m);
			output.flush();
			outbound.setData(baos.toByteArray());
			System.out.println(baos.toByteArray().length);
			if(baos.toByteArray().length <= size) {
				for(SocketAddress inet : broadcastAddress) {
					outbound.setSocketAddress(inet);
					connection.send(outbound);
				}
			}
		}
	}

	public void sendMessage(Message m, SocketAddress inet) throws IOException {
		if(!stop) {
			setupStreams();
			output.writeObject(m);
			output.flush();
			outbound.setData(baos.toByteArray());
			System.out.println(outbound.getLength());
			if(baos.toByteArray().length <= size) {
				outbound.setSocketAddress(inet);
				connection.send(outbound);
			}
		}
	}

	@Override public Message getNextMsg() {
		return null;
	}

	@Override protected void setupStreams() throws IOException {
		baos = new ByteArrayOutputStream();
		output = new ObjectOutputStream(baos);
		output.flush();
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
