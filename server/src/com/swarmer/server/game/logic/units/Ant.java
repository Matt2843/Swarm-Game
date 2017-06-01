package com.swarmer.server.game.logic.units;

import com.swarmer.server.game.logic.units.ai.AntBrain;
import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.shared.aco.graph.Node;
import com.swarmer.shared.aco.graph.Vector2;
import com.swarmer.shared.communication.Player;

public class Ant {

    public int x;
    public int y;
    private int food;

    private AntBrain brain;

    public Vector2 desiredPosition;

    public Ant(Player owner, Node startNode, Graph graph) {

        brain = new AntBrain(owner, startNode, graph);

        food = 200;
        desiredPosition = startNode.getPosition();
    }

    public void update() {
        if (food <= 0) {
            return;
        }

        desiredPosition = brain.determineNextPath().getNode();
        food -= 1;

        System.out.println(desiredPosition);
    }
}
