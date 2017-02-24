package com.swarmer.ai;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.swarmer.utility.Edge;
import com.swarmer.utility.Node;
import com.swarmer.utility.Pheromone;

public class AntBrain {
	
	/*
	 * COEFFICIENTS
	 *  TODO: What should determine the edge selection beside pheromone strength?
	 */
	
	private float c1 = 0.75f;
	
	private Edge previousEdge;
	private Node currentNode;
	private final String PLAYER_ID;
	
	public AntBrain(String PLAYER_ID, Node startingNode) {
		this.PLAYER_ID = PLAYER_ID;
		currentNode = startingNode;
	}

	public Edge determineNextPath() {
		Array<Float> edgeLikelihood = new Array<Float>();
		
		double rngesus = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

		int totalPheromones = 0;
		for(Edge evaluationEdge : currentNode.getConnectedEdges()) {
			if(!evaluationEdge.equals(previousEdge)) {
				totalPheromones += evaluationEdge.getPheromones(PLAYER_ID).getQuantity();
			}
		}
		
		float defaultProbability = (float) 1/currentNode.getConnectedEdges().size;
		for(Edge evaluationEdge : currentNode.getConnectedEdges()) {
			if(!evaluationEdge.equals(previousEdge)) {
				float pheromone = (float) evaluationEdge.getPheromones(PLAYER_ID).getQuantity();
				float decision = pheromone/totalPheromones * c1 + defaultProbability * (1 - c1);
				edgeLikelihood.add(decision);
			} else {
				edgeLikelihood.add(0.0f);
			}
		}
		
		float accumulated = 0.0f;
		int decision = 0;

		for(int i = 0; i < edgeLikelihood.size; i++) {
			float chance = (float) edgeLikelihood.get(i);
			accumulated += chance;
			if(rngesus <= accumulated){
				decision = i;
				break;
			}
		}

		Edge nextEdge = currentNode.getConnectedEdges().get(decision);

		nextEdge.getPheromones(PLAYER_ID).addPheromone();
		
		currentNode = nextEdge.getNode();
		previousEdge = nextEdge.reverse;
		return nextEdge;
	}

	public Edge getPreviousEdge() {
		return previousEdge;
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
