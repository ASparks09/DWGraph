/**
 * An adjecency matrix should be used when a graph is dense
 * because it is more efficient to store a dense graph
 * as a matrix. Storing a dense graph as a list will result
 * in requires more memory the more edges exist.
 * <p>
 * This may be different in unweighted graphs since the maximum
 * number of edges is half that of a directed graph.
 * It would still be dependent on the density of the graph
 * to determine which implementation to use.
 *
 *
 * [2.5/4.0] edge-cases
 * Graph correctly supports standard structures/edge cases: empty graph,
 * singleton, self-edge, zero-weight edge, negative-weight edge, cycles, all-
 * nodes-no-edges, and a complete graph.
 * Partially correct: Empty graph: size!=0, nodes not empty, or density()
 * not finite. Self-edge A->A not supported (weight(A,A) wrong or A missing
 * from edges(A)). Negative-weight edge not stored/retrieved exactly.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AdjMatrix implements Digraph {
    private Double[][] weights;
    private HashMap<String, Integer> keyMap;
    private Queue<Integer> queue;
    private int edgeCount;

    public AdjMatrix() {
        keyMap = new HashMap<>();
        weights = new Double[nodes().size()][nodes().size()];
        queue = new LinkedList<>();
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
        if (queue.isEmpty()) {
            resize(weights);
        }
        if (!keyMap.containsKey(key)) {
            if (!queue.isEmpty()) {
                keyMap.put(key, queue.poll());
            }
            keyMap.put(key, keyMap.size());
            return true;
        } else {
            return false;
        }
    }

    /**
     * resizes the 2d array based empty space
     * @param cur the current 2d array
     */

    public void resize(Double[][] cur) {
        Double[][] newWeights = new Double[cur.length + 10][cur.length + 10];
        for (int i = 0; i < cur.length; i++) {
            System.arraycopy(cur[i], 0, newWeights[i], 0, cur.length);
        }
        weights = newWeights;
        queue.clear();
    }
    /**
     * adds an edge to the graph
     * if graphs does not contain the source or destination node,
     * they are added to the graph
     *
     * @param src    the source node's key
     * @param dest   the destination node's key
     * @param weight the weight of the edge
     * @return true if the edge is added, false otherwise
     */
    @Override
    public boolean add(String src, String dest, Double weight) {
        if (!keyMap.containsKey(dest)) {
            add(dest);
        }
        if (!keyMap.containsKey(src)) {
            add(src);
        }

        if (weights[keyMap.get(src)][keyMap.get(dest)] != null){
            return false;
        }
        if (weights[keyMap.get(src)][keyMap.get(dest)] == null) {
            weights[keyMap.get(src)][keyMap.get(dest)] = weight;
            edgeCount++;
            return true;

        }
        return false;
    }

    /**
     * deletes a node from the graph
     * if half or more of the array is empty, the matrix is condensed
     *
     * @param key the node's key
     * @return the node's key if the node is deleted, null otherwise
     */
    @Override
    public String delete(String key) {
        if (keyMap.containsKey(key)) {
            int emptySpace = keyMap.remove(key);
            queue.add(emptySpace);
            if (queue.size() >= (weights.length/2)) {
                condensed(weights);
            }
            return key;
        }
        return null;
    }

    /**
     * condenses the matrix by removing empty space
     * @param cur the current 2d array
     */
    public void condensed(Double[][] cur) {
        Double[][] newWeights = new Double[cur.length - queue.size()][cur.length - queue.size()];
        for (int i = 0; i < cur.length; i++) {
            System.arraycopy(cur[i], 0, newWeights[i], 0, cur.length);
        }
        weights = newWeights;
        queue.clear();
    }

    /**
     * deletes an edge from the graph
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the edge if the edge is deleted, null otherwise
     */
    @Override
    public Double delete(String src, String dest) {
        if (keyMap.containsKey(src) && keyMap.containsKey(dest)) {
            Double weight = weights[keyMap.get(src)][keyMap.get(dest)];
            if (weight == null) {
                return null;
            }
            weights[keyMap.get(src)][keyMap.get(dest)] = null;
            edgeCount--;
            return weight;
        }
        return null;
    }
    /**
     * gets all nodes in the graph
     *
     * @return an ArrayList of all node keys
     */
    @Override
    public ArrayList<String> nodes() {
        if (keyMap != null) {
            return new ArrayList<>(keyMap.keySet());
        }
        return null;
    }

    /**
     * gets all outbound edges of the node in the graph
     * if the node does not exist, null is returned
     * Loop iteration is used to get all outbound edges
     *
     * @param key the node's key
     * @return an ArrayList of all outbound edge weights
     */
    @Override
    public ArrayList<String> edges(String key) {
        //is returning empty arraylist
        if (!keyMap.containsKey(key)) {
            return null;
        }
            int location = keyMap.get(key);
            ArrayList<String> EdgeWeights = new ArrayList<>();
            ArrayList<String> nodes = nodes();
            for (String dest : nodes) {
                if (weights[location][keyMap.get(dest)] != null) {
                    EdgeWeights.add(dest);
                }
            }
            return EdgeWeights;

    }

    /**
     * gets the weight of an edge
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the edge
     */
    @Override
    public Double weight(String src, String dest) {
        if (src == null || dest == null) {
            return null;
        }
        if (keyMap.containsKey(src) && keyMap.containsKey(dest)) {
            return weights[keyMap.get(src)][keyMap.get(dest)];
        }
        return null;
    }

    /**
     * gets the density of the graph
     * the density is the number of edges divided by the number of nodes squared
     * if the graph is empty, 0 is returned
     * Loop iteration is used to get the total number of edges
     *
     * @return the density of the graph
     */
    @Override
    public double density() {
        if (keyMap != null) {
            ArrayList<String> nodes = nodes();
            if (edgeCount == 0) {
                return 0.0;
            } else if (nodes.isEmpty()) {
                return 0.0;
            }
            return (double) edgeCount / (nodes.size() * (nodes.size()));
        }
        return 0.0;
    }

    /**
     * gets the density of a node
     * if the node is not in graph, 0 is returned
     *
     * @param key the node's key
     * @return the density of the node
     */
    @Override
    public double density(String key) {
        if (keyMap.containsKey(key)) {
            ArrayList<String> edges = edges(key);
            return (double) edges.size() / nodes().size();
        }
        return 0;
    }

    /**
     * gets the number of nodes in the graph
     *
     * @return the number of nodes
     */
    @Override
    public int size() {
        if (keyMap != null) {
            return keyMap.size();
        }
        return 0;
    }

    /**
     * adds associated graph type to string
     *
     * @return the string representation of the graph
     */
    @Override
    public String toString() {
        return "Adjacency Matrix:\n";
    }

    /**
     * converts the graph to JSON string
     * outer loop iterates through all nodes,
     * adding them to the JSON string
     * inner loop iterates through all edges of a node,
     * adding associated destination node and weight to JSON string
     *
     * @return the JSON representation of the graph
     */
    @Override
    public String toJSON() {
       return "Adjacency Matrix:\n";
    }
}
