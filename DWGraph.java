/**
 * Facade design pattern provides user with ability
 * to input the file they wish to turn into a graph
 * and use the most efficient implementation of the graph
 * without having to make the decision on which implementation
 * <p>
 * If DWGraph.java were to be omitted, the user would have to choose
 * which implementation to use manually. Since the purpose of
 * this file is to allow unspecificed use, as the file handles which
 * implementation to information is passed to by implementing the interface.
 */

import java.io.*;
import java.util.*;

public class DWGraph {
    private Digraph graph;
    private int size;
    private double mtxThreshold;
    private double lstThreshold;
    private String Search;

    /**
     * Constructor
     */
    public DWGraph() {
        graph = new AdjList();
        size = 0;
        mtxThreshold = 0.6;
        lstThreshold = 0.3;
    }

    /**
     * Constructor
     *
     * @param filepath path to the file
     */
    public DWGraph(String filepath) {
        if (filepath == null) {
            DWGraph graph = new DWGraph();
        } else {
        DWGraph graph = load(filepath);
        if (graph == null) {
            graph = new DWGraph();
        }
        this.graph = graph.graph;
        this.size = 0;
        mtxThreshold = 0.6;
        lstThreshold = 0.3;
        }
    }

    /**
     * Add a node to the graph
     *
     * @param key the node's key
     * @return true if the node is added, false otherwise
     */
    public boolean add(String key) {
        boolean result = this.graph.add(key);
        convert();
        return result;
    }

    /**
     * Add an edge to the graph
     *
     * @param src    the source node's key
     * @param dest   the destination node's key
     * @param weight the weight of the edge
     * @return true if the edge is added, false otherwise
     */
    public boolean add(String src, String dest, Double weight) {
        if (weight == null) {
            return false;
        } //else if (edges(dest).contains(src) || edges(src).contains(dest)) {
          //  return false;
       // }
        boolean result = this.graph.add(src, dest, weight);
        convert();
        return result;
    }

    /**
     * Delete a node from the graph
     *
     * @param key the node's key
     * @return the node's key if the node is deleted, null otherwise
     */
    public String delete(String key) {
        String result = this.graph.delete(key);
        convert();
        return result;
    }

    /**
     * Delete an edge from the graph
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the edge if the edge is deleted, null otherwise
     */
    public Double delete(String src, String dest) {
        Double result = this.graph.delete(src, dest);
        convert();
        return result;
    }

    /**
     * Get all nodes in the graph
     *
     * @return an ArrayList of node keys
     */
    public ArrayList<String> nodes() {
        if (this.graph == null) {
            return null;
        }
        return this.graph.nodes();
    }

    /**
     * Get all edges in the graph
     *
     * @param key the node's key
     * @return an ArrayList of edge keys
     */
    public ArrayList<String> edges(String key) {
        if (this.graph == null) {
            return null;
        }
        return this.graph.edges(key);
    }

    /**
     * Get the weight of an edge
     *
     * @param src  the source node's key
     * @param dest the destination node's key
     * @return the weight of the path
     */
    public Double weight(String src, String dest) {
        if (src == null || dest == null) {
            return null;
        }
        return this.graph.weight(src, dest);
    }

    /**
     * Get the density of the graph
     *
     * @return the density of the graph
     */
    public double density() {
        if (this.graph == null) {
            return 0;
        }
        return this.graph.density();
    }

    /**
     * Get the density of a node
     *
     * @param key the node's key
     * @return the density of the node
     */
    public double density(String key) {
        if (this.graph == null) {
            return 0;
        } else if (!this.graph.nodes().contains(key)) {
            return 0;
        }
        return this.graph.density(key);
    }

    /**
     * Get the number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    public int size() {
        if (this.graph == null) {
            return 0;
        }
        return this.graph.size();
    }

    /**
     * Convert the graph to a string
     * if the graph is null, return null
     *
     * @return the string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder connects = new StringBuilder();
        ArrayList<String> src = nodes();
        ArrayList<String> dest = nodes();
        if (graph != null) {
            System.out.println("Connections: ");
            for (String node : src) {
                for (String destNode : dest) {
                    if (edges(node).contains(destNode)) {
                        node = node.replace(" ", "");
                        connects.append(node).append(" -> ");
                        connects.append(destNode).append(" = ")
                                .append(weight(node, destNode))
                                .append(" ").append("\n");
                    }
                }
                connects.append("______________________\n");
            }


            connects.append("Size: ").append(size()).append("\n");
            connects.append("Density: ").append(density()).append("\n");
            return this.graph.toString() + connects.toString();
        }
        return "Graph is null";
    }

    /**
     * Convert the graph to JSON
     *
     * @return the JSON representation of the graph
     */
    public String toJSON() {
        StringBuilder jsonString = new StringBuilder("{");
        StringBuilder jsonWeight = new StringBuilder();
        ArrayList<String> src = nodes();
        ArrayList<String> dest = nodes();
        for (String node : src) {
            node = node.replace(" ", "");
            jsonString.append("\n\t\"").append(node).append("\" : {\n ");
            for (String s : dest) {
                if (weight(node, s) != null) {
                    String destNode = s.replace(" ", "");
                    jsonWeight.append("\t\t\"").append(destNode).append("\" : ")
                            .append(weight(node, destNode)).append("\n");
                }
            }

            jsonString.append(jsonWeight);
            jsonString.append("\t},\n");
            jsonWeight = new StringBuilder();
        }

        return this.graph.toJSON() + jsonString.toString();
    }

    /**
     * Load a graph from a JSON file
     *
     * @param filepath path to the JSON file
     * @return the loaded graph
     */
    public static DWGraph load(String filepath) {
        if (filepath == null) {
            return null;
        }
        try {
            Scanner sc = new Scanner(new FileReader(filepath));
            StringBuilder json = new StringBuilder();
            while (sc.hasNextLine()) {
                json.append(sc.nextLine());
            }
            sc.close();
            String jsonString = json.toString();
            if (jsonString.isEmpty()) {
                return null;
            }
            return builder(jsonString);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * * Helper method
     * Build the graph from the JSON string
     *
     * @param jsonString the JSON string
     * @return the graph
     */
    private static DWGraph builder(String jsonString) {
        DWGraph graph = new DWGraph();
        String[] jsonArray = jsonString.split("},");
        for (String s : jsonArray) {
            String table = Arrays.toString(s.split("\"", 2));
            table = reformat(table);
            String key = table.split(":", 2)[0];
            String[] keyArray = key.split("\n");
            String value = table.split(":", 2)[1];
            keyArray[0] = keyArray[0].replace(" ", "");
            keyArray[0] = keyArray[0].replace("\t", "");
            String[] dest = value.split(" ");

            for (String string : dest) {
                String destKey = string.split(":")[0];
                destKey = destKey.replace(" ", "");
                destKey = destKey.replace("\t", "");
                String weight = string.split(":")[1];
                graph.add(keyArray[0], destKey, Double.valueOf(weight));
            }

        }
        return graph;
    }


    /**
     * Helper method
     * Reformat the key to remove unwanted characters
     *
     * @param key the string containing key to be reformatted
     * @return the reformatted key
     */
    private static String reformat(String key) {
        key = key.replace("\t", "");
        key = key.replace(" ", "");
        key = key.replace(",", " ");
        key = key.replace("\"", "");
        key = key.replace("{", "");
        key = key.replace("[", "");
        key = key.replace("]", "");
        key = key.replace("}", "");
        return key;
    }

    /**
     * Convert the graph to a different implementation
     *
     */
    private void convert() {
        if (this.graph.density() > mtxThreshold) {
            AdjMatrix updatedGraph = new AdjMatrix();
            this.graph = convert(updatedGraph);
            this.size = updatedGraph.size();
        } else if (this.graph.density() < lstThreshold) {
            AdjList updatedGraph = new AdjList();
            this.graph =  convert(updatedGraph);
            this.size = updatedGraph.size();
        }
    }

    /**
     * Helper method
     * @param newGraph the graph to be updated
     * @return the updated version of the graph
     */
    private Digraph convert(Digraph newGraph) {
        for (String key : this.graph.nodes()) {
            newGraph.add(key);
            for (String dest : this.graph.edges(key)) {
                newGraph.add(key, dest, this.graph.weight(key, dest));
            }
        }
        return newGraph;
    }

    /**
     * method search(String src, String dest) that analyzes the current graph,
     * sets the Search field to the best algorithm to handle the search query,
     * and returns a Path representing the shortest path.
     * @param src
     * @param dest
     * @return
     */
    public String search(String src, String dest) {
        String all = "<ALL>";
        if (graph == null) {
            return null;
        } else if (src == null || dest == null) {
            return null;
        } else if (!this.graph.nodes().contains(src) || !this.graph.nodes().contains(dest)) {
            return null;
        }
        //might need to return path in string[]
        if(hasNegative()){
            Search = new BellmanFord().search(src,dest,graph);
        } else if(src.equals(all) && dest.equals(all)){
            Search = new FloydWarshall().search(src,dest,graph);
        }
        Search = new Dijkstras().search(src,dest,graph);
        return Search;

    }

    /**
     * Helper method
     * @return true if the graph has negative weights, false otherwise
     */
    public boolean hasNegative() {
        ArrayList<String> nodes = nodes();
        for (String node : nodes) {
            ArrayList<String> edges = edges(node);
            for (String edge : edges) {
                if (weight(node, edge) < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method
     * @return the string array representation of the path
     */
    public String[] parsePath(String path){
        String[] pathArray = path.split("\\[");
        String[] pathArray2 = pathArray[1].split("]");
        pathArray2[0] = pathArray2[0].replace(" ", "");
        return pathArray2[0].split(",");

    }

}

