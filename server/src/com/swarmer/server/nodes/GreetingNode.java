package com.swarmer.server.nodes;

/**
 * Created by Matt on 03/16/2017.
 */
public class GreetingNode extends ServerNode {

    @Override public String generateInsertQuery() {
        return "INSERT INTO greeting_nodes (id) VALUES ('" + getNodeId() + "')";
    }

    @Override
    public String getDescription() {
        return "GreetingNode";
    }

    // TODO: First client server contact, Establish connection


}
