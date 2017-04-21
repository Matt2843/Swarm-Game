package com.swarmer.shared.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class AccessPoint
{
    private static AccessPoint accessPoint;

    private AccessPoint() {
        // DO NOT INSTANTIATE THIS CLASS
    }

    public static AccessPoint getInstance() {
        if(accessPoint == null) {
            //throw new GameClientNotInstantiatedException("Please call initializeGameClient()");
            accessPoint = new AccessPoint();
        }
        return accessPoint;
    }

    public String getDatabaseControllerIP() {
        return getURLcontent("http://georgthomassen.dk/swarmer");
    }

    private static String getURLcontent(String endpoint) {
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
