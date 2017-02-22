package com.swarmer.utility;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Graph {
	private Node[][] nodes;
	private final TiledMap map;	
	private final TiledMapTileLayer collisionLayer;
	
	private final int width;
	private final int height;
	
	public Graph(TiledMap map) {
		this.map = map;
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);
		width = ((TiledMapTileLayer)map.getLayers().get(0)).getWidth();
		height = ((TiledMapTileLayer)map.getLayers().get(0)).getHeight();
		nodes = new Node[width][height];
		generateGraph();
	}

	private void generateGraph() {
		System.out.println("Kappa");
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				if(collisionLayer.getCell(i, j) == null) {
					nodes[i][j] = new Node(new Vector2(i,j));
					if(i > 0 && j > 0 && i < width-1) {
						new Edge(nodes[i][j], nodes[i-1][j]);
						new Edge(nodes[i][j], nodes[i-1][j-1]);
						new Edge(nodes[i][j], nodes[i][j-1]);
						new Edge(nodes[i][j], nodes[i+1][j-1]);
					} else if(i == 0 && j > 0) {
						new Edge(nodes[i][j], nodes[i][j-1]);
						new Edge(nodes[i][j], nodes[i+1][j-1]);
					} else if(j == 0 && i > 0) {
						new Edge(nodes[i][j], nodes[i-1][j]);
					} else if(i == width-1) {
						new Edge(nodes[i][j], nodes[i-1][j]);
						new Edge(nodes[i][j], nodes[i-1][j-1]);
						new Edge(nodes[i][j], nodes[i][j-1]);
					}
				} else {
					nodes[i][j] = new Node(new Vector2(i, j));
					nodes[i][j].isCollisionNode = true;
				}
			}
		}
		System.out.println("End Kappa");
		printNodes();
	}

	private void printNodes() {
		String row = "";
		String res = "";
		for(int j = 0; j > height; j++) {
			for(int i = 0; i < width; i++) {
				if(!nodes[i][j].isCollisionNode)
					row += "x";
				else {
					row += " ";
				}
			}
			res = row + "\n" + res;
		}
		System.out.println(res);
		
	}
}
