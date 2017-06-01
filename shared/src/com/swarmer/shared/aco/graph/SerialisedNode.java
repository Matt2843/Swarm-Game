package com.swarmer.shared.aco.graph;

import com.swarmer.shared.communication.Player;
import com.swarmer.shared.resources.Resource;

import java.io.Serializable;

public class SerialisedNode implements Serializable {

	public Player isHome;
	public Resource resource;

	public SerialisedNode(Node node) {
		isHome = node.owner;
		resource = node.resource;
	}
}
