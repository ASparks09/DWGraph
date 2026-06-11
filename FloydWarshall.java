import java.util.ArrayList;
import java.util.Arrays;

/**
 * Should be instantuated if src and dest are the exact string "<ALL>"
 */
public class FloydWarshall implements Search {
    String result = "Floyd Warshall:\n";
    String[] path;
    int cost;
    Double[][] dist;
    Integer[][] vertices;
    Boolean hasNegativeCycle = false;
     /**
     * Floyd Warshall Algorithm
     * if src or dest is not in the graph, return null
     * if src and dest are the same, return the path from src to dest
     * if any parameter is null, return null
     * @param src starting node
     * @param dest destination node
     * @param graph graph to search
     * @return shortest path from src to dest in String format and cost
     */
    @Override
    public String search(String src, String dest, Digraph graph) {
        if (src == null || dest == null || graph == null) {
            return null;
        } else if (!graph.nodes().contains(src) || !graph.nodes().contains(dest)) {
            return null;
        } else if (src.equals(dest)) {
            return result + "Shortest Path: " + src + "\n Cost: 0";
        }
        ArrayList<String> nodes = graph.nodes();
        int size = nodes.size();
        dist = new Double[size][size];
        vertices = new Integer[size][size];
        setup(nodes, graph, size);
        dist = fillGraph(dist, size);
        hasNegativeCycle = checkNegatives();
        this.cost = dist[nodes.indexOf(src)][nodes.indexOf(dest)].intValue();
        this.path = getPath(src, dest, nodes);
        return result + "Shortest Path: "
                + Arrays.toString(path) + "\nCost:" + cost;
    }

    /**
     * Setup the graph for Floyd Warshall Algorithm
     * outer for loop is for all nodes in graph
     * inner for loop is for all edges for node
     * all weights are set to the infiniity but src set to 0
     * @param nodes list of all nodes in graph
     * @param graph graph to search
     * @param size number of nodes in graph
     */
    public void setup(ArrayList<String> nodes, Digraph graph, int size){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    dist[i][j] = 0.0;
                    vertices[i][j] = j;
                } else if (graph.weight(nodes.get(i), nodes.get(j)) != null) {
                    dist[i][j] = graph.weight(nodes.get(i), nodes.get(j));
                    vertices[i][j] = j;
                }
                else {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                    vertices[i][j] = null;
                }
            }
        }
    }

    /**
     * Fills the graph with the shortest path for all nodes
     * @param dist graph to fill
     * @param size number of nodes in graph
     * @return the filled graph contains the shortest path for all nodes
     */
    public Double[][] fillGraph(Double[][] dist, int size){
        if (size == 1) {
            return dist;
        } else if (dist.length == 0) {
            return null;
        }
        for(int k = 0; k < size; k++){
            for(int i = 0; i < size; i++){
                for (int j = 0; j < size; j++) {
                    if(dist[i][k] + dist[k][j] < dist[i][j]){
                        dist[i][j] = dist[i][k] + dist[k][j];
                        vertices[i][j] = vertices[k][j];
                    }
                }
            }
        }
        return dist;
    }

    /**
     * Returns the path from src to dest
     * while loop is used to get the path from vertice array
     * @param src starting node
     * @param dest destination node
     * @param nodes list of all nodes in graph
     * @return the path from src to dest
     */
    public String[] getPath(String src, String dest, ArrayList<String> nodes){
        if (nodes.indexOf(src) == nodes.indexOf(dest)){
            return new String[]{src};
        } else if (nodes.isEmpty()){
            return new String[]{};
        }
        ArrayList<String> path = new ArrayList<>();
        int i = nodes.indexOf(src);
        int j = nodes.indexOf(dest);
        path.add(nodes.get(i));
        while(i != j){
            i = vertices[i][j];
            path.add(nodes.get(i));
        }
        //TODO:Not returning the path just source and destination
        return path.toArray(new String[path.size()]);
    }

    /**
     * Checks if there is a negative cycle in the graph
     * @return true if there is a negative cycle, false otherwise
     */
    public boolean checkNegatives(){
        for(int i = 0; i < dist.length; i++){
            if(dist[i][i] < 0){
                dist[i][i] = Double.NEGATIVE_INFINITY;
                return true;
            }
        }
        return false;
    }
}




