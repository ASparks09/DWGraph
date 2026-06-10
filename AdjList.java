/**
 * An adjecency list should be used when a graph is sparse
 * because it is more efficient to store a sparse graph as
 * a list. Storing a sparse graph as a matrix will result
 * in slower runtimes due to iterating through the matrix.
 * Iterating the list is faster.
 *
 * If a graph is unweighted, a hashmap can be used instead of a list,
 * as the weight of an edge does not matter. Hence, without storing
 * the weight of an edge, the structure is simplified.
 *
 **/

import java.util.ArrayList;
import java.util.HashMap;

class AdjList implements Digraph {
    private HashMap<String, HashMap<String, Double>> connections;
    private int edgeCount;

    public AdjList() {
        connections = new HashMap<>();
        edgeCount = 0;
    }

    /**
     * adds a node to the graph
     *
     * @param key the node's key
     * @return true if the node is added, false otherwise
     */
    @Override
    public boolean add(String key) {
        if (key == null) {
            return false;
        }
        if (!connections.containsKey(key)) {
            connections.put(key, new HashMap<>());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add an edge to the graph
     *
     * @param src    the source node's key
     * @param dest   the destination node's key
     * @param weight the weight of the edge between the nodes
     * @return true if the edge is added, false otherwise
     */
    @Override
    public boolean add(String src, String dest, Double weight) {
        if (!connections.containsKey(src)) {
            add(src);
        }
        if (!connections.containsKey(dest)) {
            add(dest);
        }
        if (connections.containsKey(src) && connections.containsKey(dest)) {
            if (connections.get(src).containsKey(dest)) {
                return false;
            }
            connections.get(src).put(dest, weight);
            edgeCount++;
            return true;
        }
        return false;
    }

    /**
     * Delete a node from the graph
     *
     * @param key the node's key
     * @return the node's key if the node is deleted, null otherwise
     */
    @Override
    public String delete(String key) {
        if (connections.containsKey(key)) {
            connections.remove(key);
            return key;
        }
        return null;
    }

    /**
     * Delete an edge from the graph
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the edge if the edge is deleted, null otherwise
     */
    @Override
    public Double delete(String src, String dest) {
        if (connections.containsKey(src) && connections.containsKey(dest)) {
            double edgeWeight = weight(src, dest);
            connections.get(src).remove(dest);
            edgeCount--;
            return edgeWeight;
        }
        return null;
    }

    /**
     * Get all nodes in the graph
     *
     * @return an ArrayList of all node keys
     */
    @Override
    public ArrayList<String> nodes() {
        if (connections != null) {
            return new ArrayList<>(connections.keySet());
        }
        return null;
    }

    /**
     * Get all outbound edges of the node in the graph
     * if the node does not exist, null is returned
     * Loop iteration is used to get all outbound edges
     * by finding main key getting all edge weights from the submap
     *
     * @param key the node's key
     * @return an ArrayList of edge weights from the node
     */
    @Override
    public ArrayList<String> edges(String key) {
        ArrayList<String> EdgeWeights = new ArrayList<>();
        if (connections.containsKey(key)) {
            ArrayList<String> nodes = nodes();
            for (String dest : nodes) {
                if (connections.get(key).get(dest) != null) {
                    EdgeWeights.add(dest);
                }
            }
            return EdgeWeights;
        }
        return null;
    }

    /**
     * Get the weight of an edge
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the edge between the nodes, null otherwise
     */
    @Override
    public Double weight(String src, String dest) {
        if (connections.containsKey(src) && connections.containsKey(dest)) {
            return connections.get(src).get(dest);
        }
        return null;
    }

    /**
     * Get the density of the graph
     * (number of edges in the graph / number of possible edges)
     * loop iteration is used to get the existing edges
     *
     * @return the density of the graph, 0 if graph is empty
     */
    @Override
    public double density() {
        if (connections != null) {
            ArrayList<String> nodes = nodes();
            if (edgeCount == 0) {
                return 0;
            }
            return (double) edgeCount / (nodes.size() * nodes.size());
        }
        return 0.0;
    }

    /**
     * Get the density of a node
     *
     * @param key the node's key
     * @return the number of existing edges
     * of the node, 0 if node is not in graph
     */
    @Override
    public double density(String key) {
        ArrayList<String> edges = edges(key);
        if (connections.containsKey(key)) {
            if (edges != null) {
                return edges.size()/(double) nodes().size();
            }
        }
        return 0;
    }

    /**
     * Get the number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    @Override
    public int size() {
        if (connections != null) {
            return connections.size();
        }
        return 0;
    }

    /**
     * Convert the graph to a string
     *
     * @return the string representation of the graph
     */
    @Override
    public String toString() {
        return "Adjacency List:\n";
    }

    /**
     * Convert the graph to JSON formatted string
     * outer loop iterates through all nodes,
     * adding them to the JSON string
     * inner loop iterates through all edges of a node,
     * adding associated destination node and weight to JSON string
     *
     * @return the JSON representation of the graph
     */
    @Override
    public String toJSON() {
        return "Adjacency List:\n";
    }
}
