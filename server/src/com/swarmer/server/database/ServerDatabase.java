package com.swarmer.server.database;

import com.swarmer.server.utility.GameEvent;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Matt on 02/27/2017.
 */
public class ServerDatabase {

    public static HashMap<String, GameEvent> currentEvents;

    public ServerDatabase() {
        try {
            configureDataStructure();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        saveData();
    }

    private void configureDataStructure() throws IOException, ClassNotFoundException {
        String path = "data/CurrentEvents.db";
        currentEvents = (HashMap<String, GameEvent>) (new File(path).exists() ? readFileToObject(path) : new HashMap<>());
    }

    private Object readFileToObject(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(new File(path));
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object target = ois.readObject();
        ois.close();
        return target;
    }

    private void saveObjectToFile(String path, Object object) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    private void updateAndSaveDatabase() {
        if(!new File("data").exists()) {
            File dir = new File("data");
            dir.mkdir();
        }
        try {
            saveObjectToFile("data/CurrentEvents.db", currentEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int minutes = 5 * 60;
                while(true) {
                    try {
                        Thread.sleep(minutes * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAndSaveDatabase();
                }
            }
        }).start();
    }

}
