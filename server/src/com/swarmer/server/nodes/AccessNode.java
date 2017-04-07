package com.swarmer.server.nodes;

import com.swarmer.server.protocols.AccessProtocol;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessNode extends ServerNode {

	private final AccessProtocol accessProtocol = new AccessProtocol();

	private SecureTCPConnection secureTCPConnection;
	private TCPConnection tcpConnection;

	protected AccessNode(int port) throws IOException {
		super(port);
	}

	@Override protected void handleConnection(Socket connection) throws IOException {
		// TODO: transition to this later, when secure channel is tested
		// secureTCPConnection = new SecureTCPConnection(connection, accessProtocol);

		tcpConnection = new TCPConnection(connection, accessProtocol);
		tcpConnection.start();
	}

	@Override public String getDescription() {
		return "Access Node";
	}
}
