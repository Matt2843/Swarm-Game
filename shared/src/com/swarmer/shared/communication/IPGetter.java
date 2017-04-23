package com.swarmer.shared.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class IPGetter
{
    private static IPGetter ipGetter;

    private IPGetter() {
        // DO NOT INSTANTIATE THIS CLASS
    }

    public static IPGetter getInstance() {
        if(ipGetter == null) {
            //throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
            ipGetter = new IPGetter();
        }
        return ipGetter;
    }

    public String getAccessUnitIP() {
        return getURLContent("http://georgthomassen.dk/swarmer/access");
    }

    public String getDatabaseControllerIP() {
        return getURLContent("http://georgthomassen.dk/swarmer/db");
    }

    private static String getURLContent(String endpoint) {
        String IP = "";
        try {
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            IP = bufferedReader.readLine();

            bufferedReader.close();

            return IP;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return IP;
    }
}
