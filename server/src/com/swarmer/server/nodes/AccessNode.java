package com.swarmer.server.nodes;

import com.swarmer.server.MotherShipCallable2;
import com.swarmer.server.protocols.AccessProtocol;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessNode extends ServerNode {

	private static final AccessProtocol accessProtocol = new AccessProtocol();
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

	private SecureTCPConnection secureTCPConnection;

	protected AccessNode(int port) {
		super(port);
	}

	public static Message getBestQualityAuthenticationNode(Message message) throws IOException, ExecutionException, InterruptedException {
		/*System.out.println(message.toString());
		Future<Message> futureResult = executorService.submit(new MotherShipCallable(message));
		System.out.println(futureResult.get());
		System.out.println("Hello albertt");
		return futureResult.get();*/
		MotherShipCallable2 msc = new MotherShipCallable2(message);
		return msc.getFutureResult();
	}

	@Override protected void handleConnection(Socket connection) throws IOException {
		// TODO: transition to this later, when secure channel is tested
		// secureTCPConnection = new SecureTCPConnection(connection, accessProtocol);
		TCPConnection tcpConnection = new TCPConnection(connection, accessProtocol);
		tcpConnection.start();
	}

	@Override public String getDescription() {
		return "access_nodes";
	}

	public static void main(String[] args) {
		new AccessNode(1111);
	}
}
