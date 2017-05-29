package com.swarmer.server.game.logic.units;

import com.swarmer.server.game.aco.ai.AntBrain;
import com.swarmer.server.game.aco.graph.Node;
import com.swarmer.server.game.aco.graph.Vector2;
import com.swarmer.shared.communication.Player;

public class Ant {

    private float x;
    private float y;
    private float food;

    private AntBrain brain;

    private Vector2 desiredPosition;

    public Ant(Player owner, Node startNode) {

        brain = new AntBrain(owner, startNode);

        food = 200;
        desiredPosition = startNode.getPosition();
    }

    public void update(float delta) {
        if (food <= 0) {
            return;
        }

        desiredPosition = brain.determineNextPath().getNode().getPosition();
        food -= 1;

        System.out.println(desiredPosition);
    }
}
