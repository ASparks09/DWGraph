/**
 * Path (a Record within Search that contains String src, String dest, an integer
 * cost and String[] path consisting of the names of vertices on the shortest path
 * from src to dest)
 */
public interface Search {
    public String search(String src, String dest, Digraph graph);
}
