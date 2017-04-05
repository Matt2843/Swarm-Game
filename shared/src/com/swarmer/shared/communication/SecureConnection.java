package com.swarmer.shared.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

public abstract class SecureConnection extends Connection {

	protected Cipher cipher;

	public SecureConnection(Socket connection) throws IOException {
		super(connection);
	}

	@Override protected void setupStreams() throws IOException {
		//cipher = new Cipher(new CipherSpi(), new Provider(name, version, info), transformation);
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			System.out.println("Sucess");
			output = new ObjectOutputStream(new CipherOutputStream(connection.getOutputStream(), cipher));
			output.flush();
			input = new ObjectInputStream(new CipherInputStream(connection.getInputStream(), cipher));
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
}