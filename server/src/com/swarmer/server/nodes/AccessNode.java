package com.swarmer.server.nodes;

import com.swarmer.server.protocols.AccessProtocol;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.MotherShipCallable;
import com.swarmer.shared.communication.SecureTCPConnection;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessNode extends ServerNode {

	private static final AccessProtocol accessProtocol = new AccessProtocol();
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

	private SecureTCPConnection secureTCPConnection;
	private TCPConnection tcpConnection;

	protected AccessNode(int port) throws IOException {
		super(port);
	}

	public static Message getBestQualityAuthenticationNode(Message message) throws IOException, ExecutionException, InterruptedException {
		Future<Message> futureResult = executorService.submit(new MotherShipCallable(message, accessProtocol));
		return futureResult.get();
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

	public static void main(String[] args) {
		try {
			AccessNode an = new AccessNode(1111);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
