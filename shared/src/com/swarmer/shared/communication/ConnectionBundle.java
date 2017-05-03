package com.swarmer.shared.communication;

/**
 * Created by Matt on 05/02/2017.
 */
public class ConnectionBundle {

	private SecureTCPConnection secureTCPConnection = null;
	private TCPConnection tcpConnection = null;
	private UDPConnection udpConnection = null;

	public ConnectionBundle() {

	}

	public ConnectionBundle(SecureTCPConnection secureTCPConnection, TCPConnection tcpConnection, UDPConnection udpConnection) {
		this.secureTCPConnection = secureTCPConnection;
		this.tcpConnection = tcpConnection;
		this.udpConnection = udpConnection;
	}

	public SecureTCPConnection getSecureTCPConnection() {
		return secureTCPConnection;
	}

	public void setSecureTCPConnection(SecureTCPConnection secureTCPConnection) {
		this.secureTCPConnection = secureTCPConnection;
	}

	public TCPConnection getTcpConnection() {
		return tcpConnection;
	}

	public void setTcpConnection(TCPConnection tcpConnection) {
		this.tcpConnection = tcpConnection;
	}

	public UDPConnection getUdpConnection() {
		return udpConnection;
	}

	public void setUdpConnection(UDPConnection udpConnection) {
		this.udpConnection = udpConnection;
	}
}
