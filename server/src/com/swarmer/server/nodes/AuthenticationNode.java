package com.swarmer.server.nodes;

import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.MotherShipCallable;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AuthenticationNode extends ServerNode {

	private final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol();
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

	protected AuthenticationNode(int port) throws IOException {
		super(port);

	}

	@Override
	protected void handleConnection(Socket connection) throws IOException {
		TCPConnection clientConnection = new TCPConnection(connection, authenticationProtocol);
		new Thread(clientConnection).start();
	}

	@Override
	public String getDescription() {
		return null;
	}

	public static boolean createUser(Message message) throws ExecutionException, InterruptedException {
		Future<Message> futureResult = executorService.submit(new MotherShipCallable(message));
		return (boolean) futureResult.get().getObject();
	}
}