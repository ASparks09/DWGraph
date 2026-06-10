/**
 * A matrix should be used when the density of the graph
 * is more than half, however this may cause runtime issues.
 * This was avoided by making the threshold for matrix
 * 2/3 of possible edges exist.
 * <p>
 * A list should be used when the density of the graph is
 * less than half, however this may cause runtime issues as well.
 * This was avoided by making the threshold for list 1/3 of possible edges exist.
 *
 **/

import java.util.ArrayList;

public interface Digraph {
    boolean add(String key);

    boolean add(String src, String dest, Double weight);

    String delete(String key);

    Double delete(String src, String dest);

    ArrayList<String> nodes();

    ArrayList<String> edges(String key);

    Double weight(String src, String dest);

    double density();

    double density(String key);

    int size();

    String toString();

    String toJSON();
}
