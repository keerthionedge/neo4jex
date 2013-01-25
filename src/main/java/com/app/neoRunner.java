package com.app;

import org.neo4j.graphdb.*;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: ramacke
 * Date: 25/1/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class neoRunner {

    public static final String MESSAGE1 = "message";

    public static void main(String[] args) {
        Node firstNode;
        Node secondNode;
        Relationship relationship;
        EmbeddedGraphDatabase graphDatabase = new EmbeddedGraphDatabase("c:\\neo4j\\exdb");
        Transaction transaction = graphDatabase.beginTx();
        try {
            firstNode = graphDatabase.createNode();
            firstNode.setProperty(MESSAGE1, "Hello");
            secondNode = graphDatabase.createNode();
            secondNode.setProperty(MESSAGE1, "World");
            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty(MESSAGE1, "knows");
            transaction.success();
        } finally {
            transaction.finish();
        }

        transaction = graphDatabase.beginTx();
        try {
            System.out.println(firstNode.getProperty(MESSAGE1));
            System.out.println(secondNode.getProperty(MESSAGE1));
            System.out.println(relationship.getProperty(MESSAGE1));
            transaction.success();
        } finally {
            transaction.finish();
        }

        transaction = graphDatabase.beginTx();
        try {
            Iterable<Node> allNodes = graphDatabase.getAllNodes();
            firstNode = graphDatabase.getReferenceNode();
            System.out.println(firstNode.getProperty(MESSAGE1));
            for (Node node : allNodes) {
                System.out.println(node.getProperty(MESSAGE1));
            }
            transaction.success();
        } finally {
            transaction.finish();
        }
        registerShutdownHook(graphDatabase);


    }


    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
