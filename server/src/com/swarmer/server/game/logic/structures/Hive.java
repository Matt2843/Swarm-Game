package com.swarmer.server.game.logic.structures;

import com.swarmer.server.game.aco.graph.Node;
import com.swarmer.server.game.aco.graph.Vector2;
import com.swarmer.shared.communication.Player;

public class Hive extends Structure {

    private final Player owner;
    private Node node;

    public Hive(Player owner, Node node) {
        this.owner = owner;
        this.node = node;
        node.setHome(owner);

        Vector2 position = node.getPosition();
    }
}
