package com.swarmer.server.protocols;

import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationProtocol extends Protocol {


	@Override protected void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException {

	}
}
