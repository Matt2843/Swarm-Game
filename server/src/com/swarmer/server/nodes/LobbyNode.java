package com.swarmer.server.nodes;

import java.util.ArrayList;

/**
 * Created by Matt on 22-03-2017.
 */
public class LobbyNode extends ServerNode {

    public LobbyNode(ArrayList<ServerNode> parents) {
        super(parents);
        setChildren(parents.get(0).getChildren().get(0).getChildren());
        for(ServerNode node : getParents()) {
            node.getChildren().add(this);
        }
    }

    @Override
    public String getDescription() {
        return "Lobby Node";
    }
}
