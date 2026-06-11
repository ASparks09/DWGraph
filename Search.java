/**
 * Path (a Record within Search that contains String src, String dest, an integer
 * cost and String[] path consisting of the names of vertices on the shortest path
 * from src to dest)
 *
 * negative weights are handled by switching the algorithm used
 * this could affect the performance of the program
 *
 */
public interface Search {
    public String search(String src, String dest, Digraph graph);
}
