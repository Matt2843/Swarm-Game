package com.swarmer.ai;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.swarmer.utility.Node;
import com.swarmer.utility.Path;
import com.swarmer.utility.Pheromone;

public class AntBrain {
	
	/*
	 * COEFFICIENTS
	 */
	
	private int c1 = 100;
	
	private Path previousPath;
	private Node currentNode;
	private final String PLAYER_ID;
	
	public AntBrain(String PLAYER_ID) {
		this.PLAYER_ID = PLAYER_ID;
	}
	
	public static void main(String[] args) {
		AntBrain ab = new AntBrain("Matt");
		
		Path p1 = new Path("p1");
		p1.getPheromones().put("Matt", new Pheromone(10));
		
		Path p2 = new Path("p2");
		p2.getPheromones().put("Matt", new Pheromone(0));
		
		Path p3 = new Path("p3");
		p3.getPheromones().put("Matt", new Pheromone(20));
		
		Path p4 = new Path("p4");
		p4.getPheromones().put("Matt", new Pheromone(5));
		
		Path p5 = new Path("p5");
		p5.getPheromones().put("Matt", new Pheromone(22));
		
//		Path p6 = new Path("p6");
//		p6.getPheromones().put("Matt", new Pheromone(8));
////		
		Array<Path> paths = new Array<Path>();
		paths.add(p1);
		paths.add(p2);
		paths.add(p3);
		paths.add(p4);
		paths.add(p5);
		//paths.add(p6);
		
		Node node = new Node(new Vector2(0,0), paths);
		ab.setCurrentNode(node);
		ab.setPreviousPath(p2);
		
		int[] scores = new int[5];
		
		for(int i = 0; i < 125000; i++) {
			
			String str = ab.determineNextPath().getPATH_ID();
			if(str.equals("p1")) {
				scores[0] += 1;
			} else if(str.equals("p2")) {
				scores[1] += 1;
			} else if(str.equals("p3")) {
				scores[2] += 1;
			} else if(str.equals("p4")) {
				scores[3] += 1;
			} else if(str.equals("p5")) {
				scores[4] += 1;
			} else if(str.equals("p6")) {
				scores[5] += 1;
			}
			
			
		}
		
		int total = 0;
		for(int i = 0; i < scores.length; i++) {
			total += scores[i];
		}
		
		for(int i = 0; i < scores.length; i++) {
			float frac = (float)scores[i]/total;
			System.out.println("P" + (i+1) + " chosen: " + frac * 100 + " " + scores[i]);
		}
	}
	
	public Path determineNextPath() {
		Array<Float> pathLikelihood = new Array<Float>();
		
		double rngesus = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
		
		int totalPheromones = 0;
		for(Path evaluationPath : currentNode.getConnectedPaths()) {
			totalPheromones += evaluationPath.getPheromones().get(PLAYER_ID).getQuantity();
		}
		
		float denominator = 0.0f;
		float defaultProbability = (float) 1/currentNode.getConnectedPaths().size;
		for(Path evaluationPath : currentNode.getConnectedPaths()) {		
			if(!evaluationPath.getPATH_ID().equals(previousPath.getPATH_ID())) {
				float pheromone = (float) evaluationPath.getPheromones().get(PLAYER_ID).getQuantity();
				float decision = pheromone/totalPheromones * c1 + defaultProbability;
				denominator += decision;
				pathLikelihood.add(decision);
			} else {
				pathLikelihood.add(0.0f);
			}
		}
		
		float accunulated = 0.0f;
		int decision = 0;
		
		for(int i = 0; i < pathLikelihood.size; i++) {
			float chance = (float) pathLikelihood.get(i) / denominator;
			accunulated += chance;
			if(rngesus <= accunulated){
				decision = i;
				break;
			}
		}
		
		//currentNode.getConnectedPaths().get(decision).getPheromones().get(PLAYER_ID).addPheromone();
		return currentNode.getConnectedPaths().get(decision);
	}

	public Path getPreviousPath() {
		return previousPath;
	}

	public void setPreviousPath(Path previousPath) {
		this.previousPath = previousPath;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

}
