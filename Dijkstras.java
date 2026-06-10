
import java.util.*;

/**
 * Should be instantiated if src is the name of a vertex in the graph,
 * dest is the name of a vertex in the graph, and there are no negative
 * edge weights in the graph.
 */
public class Dijkstras implements Search{
    String result = "Dijkstra's:\n";
    String[] path;
    Double cost;
    ArrayList<String> knownNode= new ArrayList<>();
    HashMap<String, Node> dist = new HashMap<>();
    PriorityQueue<Node> pq = new PriorityQueue<>();

    public class Node implements Comparable<Node>{
        String name;
        Boolean known;
        String[] path;
        Double cost;

        /**
         * Constructor for Node class
         * containing name of node,
         * whether it is known,
         * how it was reached/backtrace (path), and cost to get there
         * @param name
         */
        public Node(String name){
            this.name = name;
            this.known = false;
            this.path = new String[]{name};
            this.cost = Double.MAX_VALUE;
        }

        /**
         * Compare two nodes based on their cost
         * required to override compareTo method in Comparable interface
         * @param other the object to be compared.
         * @return 1 if this is less than other, 0 if equal, -1 if greater
         */
        @Override
        public int compareTo(Node other){
            return Double.compare(this.cost, other.cost);
        }
    }

    /**
     * Search for the shortest path between src and dest
     * Uses Queue to keep track of the nodes to be processed by lowest cost first
     * Uses HashMap to keep track of the nodes and their distances
     *
     * while the queue is not empty get the node with the lowest cost
     * if the node is not known mark it as known and add it to the knownNode list
     * if the node is the destination path exists between src and dest directly
     * for loop through all edges of the node if the cost to get to the edge is
     * lower than the current cost update the cost and path of the node and add it to the queue
     *
     * @param src starting node
     * @param dest ending node
     * @param graph graph to search
     * @return shortest path from src to dest in String format
     */
    @Override
    public String search(String src, String dest, Digraph graph) {
        if (src == null || dest == null || graph == null) {
            return null;
        }
        setup(src, graph);
        while(!pq.isEmpty()) {
            Node lowestCost = pq.poll();
            if(lowestCost.known) {
                continue;
            }
            lowestCost.known = true;
            knownNode.add(lowestCost.name);
            if(lowestCost.name.equals(dest)) {
                path = lowestCost.path;
                cost = lowestCost.cost;
                break;
            }
            for(String edge : graph.edges(lowestCost.name)) {
                if(!knownNode.contains(edge)) {
                    Node n = dist.get(edge);
                    double newCost = lowestCost.cost + graph.weight(lowestCost.name, edge);
                    if(newCost < n.cost) {
                        n.cost = newCost;
                        n.path = Arrays.copyOf(lowestCost.path, lowestCost.path.length + 1);
                        n.path[n.path.length - 1] = edge;
                        pq.add(n);
                    }
                }
            }
        }
        //May need to return string array of path
        return result + "Shortest Path: "
                + Arrays.toString(path) + "\nCost:" + cost;
    }

    /**
     * Setup the graph for Dijkstra's algorithm
     * 1. Initialize the source node
     * 2. Initialize the distance map
     * 3. Add the source node to the priority queue
     * 4. Add all other nodes to the distance map (for loop)
     * @param src source node, initalized to 0 as already at source
     * @param graph graph to search
     */
    public void setup(String src, Digraph graph){
        Node start = new Node(src);
        start.cost = 0.0;
        dist.put(src, start);
        pq.add(start);
        for (String node : graph.nodes()) {
            if(!dist.containsKey(node)) {
                dist.put(node, new Node(node));
            }
        }
    }
}
