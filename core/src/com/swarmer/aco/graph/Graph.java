package com.swarmer.utility;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Graph {
	public Node[][] nodes;
	private final TiledMapTileLayer collisionLayer;
	
	private final int width;
	private final int height;
	
	public Graph(TiledMap map) {
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);
		width = ((TiledMapTileLayer)map.getLayers().get(0)).getWidth();
		height = ((TiledMapTileLayer)map.getLayers().get(0)).getHeight();
		nodes = new Node[width][height];
		generateGraph();
	}

	private void generateGraph() {
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				if(collisionLayer.getCell(i, j) == null) {
					nodes[i][j] = new Node(new Vector2(i,j));
					if(i > 0 && j > 0 && i < width-1) {
						new Edge(nodes[i][j], nodes[i-1][j], true);
						new Edge(nodes[i][j], nodes[i-1][j-1], true);
						new Edge(nodes[i][j], nodes[i][j-1], true);
						new Edge(nodes[i][j], nodes[i+1][j-1], true);
					} else if(i == 0 && j > 0) {
						new Edge(nodes[i][j], nodes[i][j-1], true);
						new Edge(nodes[i][j], nodes[i+1][j-1], true);
					} else if(j == 0 && i > 0) {
						new Edge(nodes[i][j], nodes[i-1][j], true);
					} else if(i == width-1) {
						new Edge(nodes[i][j], nodes[i-1][j], true);
						new Edge(nodes[i][j], nodes[i-1][j-1], true);
						new Edge(nodes[i][j], nodes[i][j-1], true);
					}
				}
			}
		}
		//printNodes();
	}

	private void printNodes() {
		String res = "";
		for(int j = 0; j < height; j++) {
			String row = "";
			for(int i = 0; i < width; i++) {
				if(nodes[i][j] != null){
					row = row + "x";
				} else {
					row = row + " ";
				}
			}
			res = row + "\n" + res;
		}
		System.out.print(res);
		
		for(Edge e : nodes[0][0].getConnectedEdges()) {
			//System.out.println("N1: " + e.getNode1().getPosition().toString() + " N2: " + e.getNode2().getPosition().toString());
		}
		
	}
}
