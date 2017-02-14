package com.swarmer.ai;

import com.swarmer.utility.Node;

public class AntBrain {
	
	private Node previousNode;
	private Node currentNode;
	
	public Node determineNextNode() {
		/*
		 * set previousNode coefficient low
		 * roll through the nodesInVicinity from currentNode,
		 * according to the pheromone strengths.
		 * return the node.
		 */
		return null;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

}
