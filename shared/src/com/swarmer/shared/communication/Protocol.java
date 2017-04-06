package com.swarmer.shared.communication;

import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public abstract class Protocol {

	protected abstract void react(Message message, Connection caller) throws IOException, SQLException;

}
