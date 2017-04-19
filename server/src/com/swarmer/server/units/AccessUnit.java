package com.swarmer.server.units;

import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.protocols.AccessProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessUnit extends ServerUnit {

	private final AccessProtocol accessProtocol = new AccessProtocol(this);

	private AccessUnit() {
		super();
	}

	@Override protected int getPort() {
		return ServerUnit.ACCESS_UNIT_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return accessProtocol;
	}

	public static Message getBestQualityAuthenticationNode(Message message) throws IOException, ExecutionException, InterruptedException {
		/*System.out.println(message.toString());
		Future<Message> futureResult = executorService.submit(new MotherShipCallable(message));
		System.out.println(futureResult.get());
		System.out.println("Hello albertt");
		return futureResult.get();*/
		DatabaseControllerCallable msc = new DatabaseControllerCallable(message);
		return msc.getFutureResult();
	}

	@Override public String getDescription() {
		return "access_units";
	}

	public static void main(String[] args) {
		new AccessUnit();
	}
}
