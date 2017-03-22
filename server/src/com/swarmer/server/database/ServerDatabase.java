package com.swarmer.server.database;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.nodes.GameNode;
import com.swarmer.server.nodes.GreetingNode;
import com.swarmer.server.nodes.ServerNode;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Matt on 02/27/2017.
 */
public class ServerDatabase {

    public static HashMap<Player, String> playerDatabase;
    public static HashMap<String, ServerNode> serverNodes;

    private ServerDatabase() {
        // PREVENT INSTANTIATION.
    }

    public static void initializeDatabase() {
        try {
            setupHashMaps();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        saveData();
    }

    private static void setupHashMaps() throws IOException, ClassNotFoundException {
        String path = "data/PlayerDatabase.db";
        playerDatabase = (HashMap<Player, String>) (new File(path).exists() ? readFileToObject(path) : new HashMap<>());
        path = "data/ServerNodes.db";
        serverNodes = (HashMap<String, ServerNode>) (new File(path).exists() ? readFileToObject(path) : new HashMap<>());
    }

    public static void createServerNode(ServerNode serverNode) throws UnkownServerNodeException {
        if(serverNode instanceof GameNode
                ||  serverNode instanceof AuthenticationNode
                ||  serverNode instanceof GreetingNode) {
            serverNode.start();
            serverNodes.put(serverNode.getNodeId(), serverNode);
        } else {
            throw new UnkownServerNodeException();
        }
    }

    private static Object readFileToObject(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(new File(path));
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object target = ois.readObject();
        ois.close();
        return target;
    }

    private static void saveObjectToFile(String path, Object object) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    private static void updateAndSaveDatabase() {
        if(!new File("data").exists()) {
            File dir = new File("data");
            dir.mkdir();
        }
        try {
            saveObjectToFile("data/CurrentEvents.db", playerDatabase);
            saveObjectToFile("data/ServerNodes.db", serverNodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int minutes = 5 * 60;
                while(true) {
                    updateAndSaveDatabase();
                    try {
                        Thread.sleep(minutes * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
