package com.swarmer.shared.resources;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Matt on 03/29/2017.
 */
public class test {

    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;
        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        byte[] salt = new byte[32];
        String password = "Burger";
        char[] pw = password.toCharArray();
        int iterations = 10;
        int keylenght = 256;
        try {
            SecureRandom.getInstanceStrong().nextBytes(salt);
            System.out.println(bytesToHex(hashPassword(pw, salt, iterations, keylenght)));



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
