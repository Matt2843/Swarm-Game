package com.swarmer.shared.aco.graph;

import java.io.Serializable;

public class SerialisedGraph implements Serializable {
    private static final long serialVersionUID = -1338L;
	public SerialisedNode[][] nodes;

	public SerialisedGraph(Graph graph) {
		nodes = new SerialisedNode[graph.nodes.length][graph.nodes[0].length];
		for(int i = 0; i < graph.nodes.length; i++) {
			for(int j = 0; j < graph.nodes[0].length; j++) {
				nodes[i][j] = new SerialisedNode(graph.nodes[i][j]);
			}
		}
	}
}
