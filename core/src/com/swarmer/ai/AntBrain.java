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
	
	private float c1 = (float) 0.9;
	
	private Edge previousEdge;
	private Node currentNode;
	private final String PLAYER_ID;
	
	public AntBrain(String PLAYER_ID, Node startingNode) {
		this.PLAYER_ID = PLAYER_ID;
		currentNode = startingNode;
	}

	public Edge determineNextPath() {
		Array<Float> pathLikelihood = new Array<Float>();
		
		double rngesus = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

		int totalPheromones = 0;
		for(Edge evaluationPath : currentNode.getConnectedEdges()) {
			Pheromone pheromones = evaluationPath.getPheromones().get(PLAYER_ID);
			if(pheromones != null) {
				totalPheromones += pheromones.getQuantity();
			}
		}
		
		float defaultProbability = (float) 1/currentNode.getConnectedEdges().size;
		for(Edge evaluationPath : currentNode.getConnectedEdges()) {		
			if(!evaluationPath.equals(previousEdge)) {
				Pheromone pheromones = evaluationPath.getPheromones().get(PLAYER_ID);
				float pheromone = 0;
				if(pheromones != null) {
					pheromone = (float) pheromones.getQuantity();
				}
				float decision = pheromone/totalPheromones * c1 + defaultProbability * (1 - c1);
				pathLikelihood.add(decision);
			} else {
				pathLikelihood.add(0.0f);
			}
		}
		
		float accumulated = 0.0f;
		int decision = 0;

		for(int i = 0; i < pathLikelihood.size; i++) {
			float chance = (float) pathLikelihood.get(i);
			accumulated += chance;
			if(rngesus <= accumulated){
				decision = i;
				break;
			}
		}

		Edge nextEdge = currentNode.getConnectedEdges().get(decision);

		if(!nextEdge.getPheromones().containsKey(PLAYER_ID)){
			nextEdge.getPheromones().put(PLAYER_ID, new Pheromone(1));
		} else {
			nextEdge.getPheromones().get(PLAYER_ID).addPheromone();
		}

		currentNode = nextEdge.getNode();
		previousEdge = nextEdge.reverse;
		return nextEdge;
	}

	public Edge getPreviousPath() {
		return previousEdge;
	}

	public void setPreviousPath(Edge previousPath) {
		this.previousEdge = previousPath;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

}
