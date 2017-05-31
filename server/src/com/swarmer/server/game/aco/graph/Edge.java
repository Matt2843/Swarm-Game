package com.swarmer.server.game.aco.graph;


import com.swarmer.shared.communication.Player;

import java.io.Serializable;
import java.util.HashMap;

public class Edge implements Serializable {

    private HashMap<Player, Pheromone> pheromones;
    private String PATH_ID;

    public Edge reverse;

    private final Node dest;

    public Edge(Node start, Node end, Boolean bool) {
        dest = end;
        if(start != null && end != null) {
            start.addEdge(this);
            if(bool) {
                pheromones = new HashMap<>();
                reverse = new Edge(end, start, false);
                reverse.setPheromones(pheromones);
                reverse.reverse = this;
            }
        }
    }

    public String getPATH_ID() {
        return PATH_ID;
    }

    public Pheromone getPheromones(Player player) {
        if(!pheromones.containsKey(player)) {
            pheromones.put(player, new Pheromone(0));
        }
        return pheromones.get(player);
    }

    public HashMap<Player, Pheromone> getPheromones() {
        return pheromones;
    }

    public void setPheromones(HashMap<Player, Pheromone> pheromones) {
        this.pheromones = pheromones;
    }

    @Override public boolean equals(Object edge) {
        return edge != null && dest.equals(edge);
    }

    public Node getNode() {
        return dest;
    }
}
