package com.swarmer.shared.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecureTCPConnection extends TCPConnection {
	protected Cipher cipher;

	public SecureTCPConnection(Socket connection, Protocol protocol) throws IOException {
		super(connection, protocol);
	}

	@Override protected void setupStreams() throws IOException {
		//cipher = new Cipher(new CipherSpi(), new Provider(name, version, info), transformation);
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			SecretKeySpec skeySpec = new SecretKeySpec(KeyGenerator.getInstance("AES").generateKey().getEncoded(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			output = new ObjectOutputStream(new CipherOutputStream(connection.getOutputStream(), cipher));
			output.flush();
			CipherInputStream c = new CipherInputStream(connection.getInputStream(), cipher);
			input = new ObjectInputStream(c);

		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(NoSuchPaddingException e) {
			e.printStackTrace();
		} catch(InvalidKeyException e) {
			e.printStackTrace();
		}
	}
}
