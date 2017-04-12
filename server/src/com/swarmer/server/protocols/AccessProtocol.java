package com.swarmer.server.protocols;

import com.swarmer.server.nodes.AccessNode;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AccessProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException {
		this.caller = caller;
		System.out.println("Access node protocol: " + message.toString());

		switch (message.getOpcode()) {
			case 1: // request best quality authentication_node from DB through mothership
				getAuthenticationNode(new Message(1, "authentication_nodes"));
				break;
			default:
				break;
		}
	}

	private void getAuthenticationNode(Message message) {
		try {
			Message sqlRespond = AccessNode.getBestQualityAuthenticationNode(message);
			System.out.println(sqlRespond.toString());
			caller.sendMessage(sqlRespond);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
