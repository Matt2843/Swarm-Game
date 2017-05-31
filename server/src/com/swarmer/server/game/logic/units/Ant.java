package com.swarmer.server.game.logic.units;

import com.swarmer.shared.aco.ai.AntBrain;
import com.swarmer.shared.aco.graph.Node;
import com.swarmer.shared.aco.graph.Vector2;
import com.swarmer.shared.communication.Player;

public class Ant {

    public int x;
    public int y;
    private int food;

    private AntBrain brain;

    public Vector2 desiredPosition;

    public Ant(Player owner, Node startNode) {

        brain = new AntBrain(owner, startNode);

        food = 200;
        desiredPosition = startNode.getPosition();
    }

    public void update() {
        if (food <= 0) {
            return;
        }

        desiredPosition = brain.determineNextPath().getNode().getPosition();
        food -= 1;

        System.out.println(desiredPosition);
    }
}
