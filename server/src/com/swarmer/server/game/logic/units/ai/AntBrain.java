package com.swarmer.server.game.logic.units.ai;

import com.swarmer.shared.aco.graph.Edge;
import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.shared.aco.graph.Node;
import com.swarmer.shared.aco.graph.Vector2;
import com.swarmer.shared.communication.Player;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AntBrain {

	/*
	 * COEFFICIENTS
	 *  TODO: What should determine the edge selection beside pheromone strength?
	 */

    private float C1 = 0.8f;
    private float c1;

    private String state = "Seek";
    private Edge previousEdge;
    private Edge nextEdge;
    private Node currentNode;
	private Graph graph;
	private Node previousNode;
    private final Player owner;

    public AntBrain(Player owner, Node startingNode, Graph graph) {
        this.owner = owner;
        currentNode = startingNode;
		this.graph = graph;
		previousNode = currentNode;
        c1 = C1;
    }

    public Edge determineNextPath() {
        double rngesus = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

        ArrayList<Float> edgeLikelihood = getEdgeLikelihood(getTotalPheromones());

        float accumulated = 0.0f;
        int decision = 0;

        for(int i = 0; i < edgeLikelihood.size(); i++) {
            accumulated += edgeLikelihood.get(i);
            if(rngesus <= accumulated) {
                decision = i;
                break;
            }
        }

        nextEdge = currentNode.getConnectedEdges().get(decision);

        previousNode = currentNode;
        Vector2 index = nextEdge.getNode();
        currentNode = graph.nodes[index.x][index.y];

        if(currentNode.hasResource() && currentNode.getResource().getQuantity() > 0) {
            c1 = 1f;
            state = "Returning";
        }

        if(currentNode.isHome(owner)) {
            c1 = C1;
            state = "Seek";
        }

        if(state.equals("Returning")){
            nextEdge.getPheromones(owner).addPheromone(100);
        } else if(state.equals("Seek")) {
            nextEdge.getPheromones(owner).addPheromone(1);
        }

        previousEdge = nextEdge.reverse;
        return nextEdge;
    }

    private int getTotalPheromones() {
        int totalPheromones = 0;
        for(Edge evaluationEdge : currentNode.getConnectedEdges()) {
            if(evaluationEdge != previousEdge) {
                totalPheromones += evaluationEdge.getPheromones(owner).getQuantity();
            }
        }
        return totalPheromones;
    }

    private ArrayList<Float> getEdgeLikelihood(int totalPheromones) {
        float decision;
        ArrayList<Float> edgeLikelihood = new ArrayList<Float>();
        float defaultProbability = (float) 1 / (currentNode.getConnectedEdges().size() - 1);
        for(Edge evaluationEdge : currentNode.getConnectedEdges()) {
            if(evaluationEdge != previousEdge) {
                if(totalPheromones > 0) {
                    float pheromone = (float) evaluationEdge.getPheromones(owner).getQuantity();
                    decision = pheromone / totalPheromones * c1 + defaultProbability * (1 - c1);
                } else {
                    decision = defaultProbability;
                }
                edgeLikelihood.add(decision);
            } else {
                edgeLikelihood.add(0.0f);
            }
        }
        return edgeLikelihood;
    }

    public Edge getPreviousEdge() {
        return previousEdge;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousEdge(Edge previousEdge) {
        this.previousEdge = previousEdge;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

}

