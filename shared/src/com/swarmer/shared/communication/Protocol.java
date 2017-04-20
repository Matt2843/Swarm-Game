package com.swarmer.shared.communication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public abstract class Protocol {

	public PublicKey exPublicKey;

	protected abstract void react(Message message, Connection caller) throws IOException, SQLException, NoSuchAlgorithmException;

}
