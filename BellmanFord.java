import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Collections.reverse;


/**
 * Should be instantiated if src is the name of a vertex in the graph,
 * dest is the name of a vertex in the graph, and there are negative
 * edge weights in the graph.
 */
public class BellmanFord implements Search {
    String result = "Bellman-Ford:\n";
    String[] path;
    int cost;
    Double[] dist;
    String[] vertices;
    String[] backtrace;

    /**
     * searches for the shortest path between src and dest in graph
     * if there is a negative cycle, return "Negative Cycle Detected"
     * if any parameter is null, return null
     * if src or dest are not in graph, return null
     *
     * @param src   source node
     * @param dest  destination node
     * @param graph graph to search
     * @return shortest path from src to dest in String format and cost
     */
    @Override
    public String search(String src, String dest, Digraph graph) {
        if (src == null || dest == null || graph == null) {
            return null;
        } else if (!graph.nodes().contains(src) || !graph.nodes().contains(dest)) {
            return null;
        }
        setup(src, graph);
        int size = graph.nodes().size();
        backtrace = new String[size];
        //couldn't figure out how to do this without 3 for loops
        for (int i = 0; i < size - 1; i++) {
            for (String node : graph.nodes()) {
                for (String edge : graph.edges(node)) {
                    Double weight = graph.weight(node, edge);
                    int nodeDist = graph.nodes().indexOf(node);
                    int edgeDist = graph.nodes().indexOf(edge);
                    if (dist[edgeDist] > (int) (dist[nodeDist] + weight)) {
                        dist[edgeDist] = (dist[nodeDist] + weight);
                        backtrace[edgeDist] = node;
                    }
                }
            }
        }
        if (checkNegCycle()) {
            return result + "Negative Cycle Detected";
        }
        path = getPath(src, dest, graph);
        Double costResult = dist[graph.nodes().indexOf(dest)];
        cost = costResult.intValue();
        return result + "Shortest Path: "
                + Arrays.toString(path) + "\nCost:" + cost;
    }

    /**
     * setup the graph for Bellman-Ford Algorithm
     * all weights are set to infinity except src set to 0
     *
     * @param src   source node
     * @param graph graph to search
     */
    public void setup(String src, Digraph graph) {
        vertices = graph.nodes().toArray(new String[0]);
        dist = new Double[graph.nodes().size()];
        for (int i = 0; i < graph.nodes().size(); i++) {
            dist[i] = Double.MAX_VALUE;
        }
        dist[graph.nodes().indexOf(src)] = 0.0;
    }

    /**
     *
     * @param src   1st node in path
     * @param dest  2nd node in path
     * @param graph graph to search
     * @return the shortest path from src to dest
     */
    public String[] getPath(String src, String dest, Digraph graph) {
        ArrayList<String> path = new ArrayList<>();
        String cur = dest;
        while (!Objects.equals(cur, src)) {
            path.add(cur);
            cur = backtrace[graph.nodes().indexOf(cur)];
        }
        path.add(src);
        reverse(path);
        return path.toArray(new String[0]);
    }

    /**
     *
     * @return true if there is a negative cycle, false otherwise
     */
    public boolean checkNegCycle() {
        for (Double value : dist) {
            if (value < 0) {
                return true;
            }
        }
        return false;
    }
}
