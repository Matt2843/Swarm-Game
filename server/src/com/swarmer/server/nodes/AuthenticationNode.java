package com.swarmer.server.nodes;

import com.swarmer.server.protocols.AuthenticationProtocol;
import com.swarmer.shared.communication.Message;
import com.swarmer.server.MotherShipCallable;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AuthenticationNode extends ServerNode {

	private static final AuthenticationProtocol authenticationProtocol = new AuthenticationProtocol();
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

	protected AuthenticationNode(int port) throws IOException {
		super(port);
	}

	public static boolean createUser(Message message) throws ExecutionException, InterruptedException, IOException {
		Future<Message> futureResult = executorService.submit(new MotherShipCallable(message, authenticationProtocol));
		return (boolean) futureResult.get().getObject();
	}

	public static void authenticateUser(Message message) {

	}

	@Override
	protected void handleConnection(Socket connection) throws IOException {
		TCPConnection clientConnection = new TCPConnection(connection, authenticationProtocol);
		clientConnection.start();
	}

	@Override
	public String getDescription() {
		return "authentication_nodes";
	}

	public static void main(String[] args) {
		try {
			new AuthenticationNode(1112);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}