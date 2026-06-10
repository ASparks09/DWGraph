import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.util.Collections.min;

/**
 * Should be instantiated if src is the name of a vertex in the graph,
 * dest is the name of a vertex in the graph, and there are negative
 * edge weights in the graph.
 */
public class BellmanFord implements Search{
    String result = "Bellman-Ford:\n";
    String[] path;
    int cost;
    Integer[] dist;
    String[] vertices;

    @Override
    public String search(String src, String dest, Digraph graph) {
        if (src == null || dest == null || graph == null) {
            return null;
        } else if (!graph.nodes().contains(src) || !graph.nodes().contains(dest)) {
            return null;}
        setup(src, graph);

        int size = graph.nodes().size() - 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int newCost = getCost(vertices[j], vertices[i], graph);
                if (newCost < dist[i]) {
                    dist[i] = newCost;
                    vertices[i] = vertices[j];
                }
            }
        }
        return result + "Shortest Path: "
                + Arrays.toString(path) + "\n Cost:" + cost;
    }


    public void setup(String src, Digraph graph){
        dist = new Integer[graph.nodes().size()];

        for(int i = 0; i < graph.nodes().size(); i++){
            dist[i] = Integer.MAX_VALUE;
            vertices[i] = graph.nodes().get(i);
        }
        dist[graph.nodes().indexOf(src)] = 0;
    }

    public int getCost(String src, String dest, Digraph graph){
        ArrayList<String> nodes = graph.nodes();
        int srcIndex = nodes.indexOf(src);
        int destIndex = nodes.indexOf(dest);
        Double weight = graph.weight(src, dest);
        if(weight != null){
            return dist[srcIndex] + weight.intValue();
        }
        return dist[srcIndex];

    }
}
