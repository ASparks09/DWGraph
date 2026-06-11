import java.util.ArrayList;
import java.util.Scanner;

/**
 * What was your process to create the game? What problems did you encounter and how
 * did you solve them?
 * Process:
 * Breaking down into smaller parts that slowly built up into the require game
 * Problems:
 * interaction with the user requires handling of invalids inputs
 * there is current still an issue where when only the 2 random nodes
 * are in the path an infinite loop occurs
 * Solutions:
 * require loops to run until user input was valid format
 * reformating strings to match graph nodes names to solve case sensitivity issues
 * for infinite loop i will change the condition currently checking if input
 * is blank or nextline as it is not registering
 *
 * If you could change the game rules, what would you do and why?
 * likely the gamble mode point reward changing, currently it is based on
 * the difference between the number of connections and the guess
 * meaning one could deduce the number of connections based on how many points were lost
 * could be changes to a number of points user wants to "wager" making it more difficult
 * and more "gamble" related, possible even allowing extra based on the wager if guess
 * is correct (double points on first guess correct, etc)
 */

public class Baconator extends DWGraph {
    static int points = 9;
    static int mode;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DWGraph graph = new DWGraph("src\\16-node-sample.json");
        System.out.println("Welcome to Baconator\n");
        if (graph.nodes().size() <=9) {
            mode = 0;
        } else {
            mode = 1;
        }

        //TODO: change game mode based on # of nodes
        printNodes(mode, graph, scan);
    }

    /**
     * Generates 2 random nodes and the path between them
     *
     * @param graph graph to search
     * @return the path between the 2 nodes
     */
    public static String[] getPaths(DWGraph graph) {
        String src = randomNodes(graph);
        String dest = randomNodes(graph);
        String output = graph.search(src, dest);
        return graph.parsePath(output);
    }

    /**
     * Generates a random node from the graph
     *
     * @param graph graph to search
     * @return a random node from node ArrayList
     */
    public static String randomNodes(DWGraph graph) {
        ArrayList<String> nodes = graph.nodes();
        int rand = (int) (Math.random() * nodes.size());
        return nodes.get(rand);
    }

    /**
     * Prints instructions based on the game mode
     * calls the appropriate game mode function
     *
     * @param mode  game mode
     * @param graph graph to search
     * @param scan  scanner to read input from the user
     */
    public static void printNodes(int mode, DWGraph graph, Scanner scan) {
        if (mode == 1) {
            System.out.println("Welcome to Gamble Mode\n");
            System.out.println("You will be given two random nodes and start with 9 points.");
            System.out.println("Your task is to guess how many connections are between them." +
                    "Points will be deducted for an incorrect guess.");
            int counter = 0;
            gambleMode(counter, graph, scan);
        } else {
            System.out.println("Welcome to Guess Mode\n");
            System.out.println("You will be given two random nodes and start with 9 points.");
            System.out.println("Your task is to guess the nodes are between them.\n" +
                    "Points will be deducted for every incorrect guess.");
            int counter = 0;
            guessMode(counter, graph, scan);
        }
    }

    /**
     * game mode for gamble mode
     * prompt user to enter the number of connections between the 2 nodes
     * check if the user's guess is correct
     * checks if the user has enough points to keep playing
     * if not, the game is over
     * if yes, the game continues to next round
     *
     * @param counter round counter
     * @param graph   graph to search
     * @param scan    scanner to read input from the user
     */
    public static void gambleMode(int counter, DWGraph graph, Scanner scan) {
        counter++;
        System.out.println("Round #" + counter);
        System.out.println("Total number of nodes:" + graph.size() + "\n");
        String[] path = getPaths(graph);
        System.out.println("Current number of points remaining: " + points);
        System.out.println("2 Random Nodes have been selected, " +
                "enter the number of connections between them:\n");
        while (!scan.hasNextInt()) {
            scan.next();
            System.out.println("Invalid Input, please enter a valid number of connections:");
        }
        int guess = scan.nextInt(); //Exception handling for invalid input
        checkConnections(path, guess);
        if (points <= 0) {
            System.out.println("You have run out of points, the game is over.");
            System.exit(0);
        } else {
            gambleMode(counter, graph, scan);
        }

    }

    /**
     * game mode for guess mode
     * prompt user to enter a node name
     * check if the user's guess is correct
     * repeat the process until the user has guessed all between the 2 nodes
     * checks if the user has enough points to keep playing
     * if not, the game is over
     * if yes, the game continues to next round
     *
     * @param counter round counter
     * @param graph   graph to search
     * @param scan    scanner to read input from the user
     */
    public static void guessMode(int counter, DWGraph graph, Scanner scan) {
        counter++;
        System.out.println("Round #" + counter);
        System.out.println("All Nodes:\n" + graph.nodes() + "\n");
        String[] path = getPaths(graph);
        if (path[0].equals(path[path.length - 1])) {
            guessMode(counter, graph, scan);
        }
        System.out.println("Nodes:" + path[0] + path[path.length - 1]);
        String guess = userGuess(graph, path, scan);
        boolean isBetween = checkPath(graph, path, guess);
        int connections = path.length - 2;
        if (isBetween) {
            connections--;
        }
        while (connections != 0) {
            System.out.println("Current number of points remaining: " + points);
            System.out.println("There are still connections between the two nodes. " +
                    "Enter another node name:\n");
            guess = userGuess(graph, path, scan);
            isBetween = checkPath(graph, path, guess);
            if (isBetween) {
                connections--;
            }
        }
        if (points > 0) {
            System.out.println("Congratulations, ypu have enough points to keep playing.");
            guessMode(counter, graph, scan);
        }
        //TODO: Infinity loop if direct link between 2 nodes
    }

    /**
     * Checks if the user's guess is in the path
     * if yes, points are added
     * if no, points are deducted
     * if the user has no points left, the game is over
     *
     * @param graph graph to search
     * @param path  path between the 2 nodes
     * @param guess user's guess
     * @return true if the user's guess is in the path, false otherwise
     */
    public static boolean checkPath(DWGraph graph, String[] path, String guess) {
        for (String node : path) {
            if (node.equals(guess)) {
                points++;
                return true;
            }
        }
        points--;
        System.out.println("Incorrect, you have " + points + " points remaining.");
        if (points <= 0) {
            System.out.println("You have run out of points, the game is over.");
            System.exit(0);
        }
        return false;
    }

    /**
     * checks if the user's guess is correct
     * if yes, points are added
     * if no, points are deducted
     * if difference is negative, it is added to points
     * if difference is positive, it is subtracted from points
     * preventing accidental double negatives that give points
     *
     * @param path
     * @param guess
     */
    public static void checkConnections(String[] path, int guess) {
        int connections = path.length - 2;
        if (connections == guess) {
            points = points + connections;
            System.out.println("Correct, you have " + points + " points remaining.");
        } else {
            int difference = guess - connections;
            if (difference < 0) {
                points = points + difference;
            } else {
                points = points - difference;
            }
            System.out.println("Incorrect, you have " + points + " points remaining.");
        }
    }

    /**
     * Checks if the user's guess is a valid node name
     * reformats the user's guess to match the node names in the graph
     * if the user's guess is a valid node name, it is returned
     *
     * @param graph graph to search
     * @param path  path between the 2 nodes
     * @param scan  scanner to read input from the user
     * @return the user's guess
     */
    public static String userGuess(DWGraph graph, String[] path, Scanner scan) {
        System.out.println("Enter a node name to check if it is between the two nodes:");
        String guess = scan.next();
        guess = reformat(guess);
        System.out.println(guess);
        if ((guess.equals("\n") || guess.equals(" ") && path.length == 2)) {
            points++;
            return "Correct, there is a direct link";
        }//TODO: doesn't currently handle exception correctly (see other todo)
        while (!graph.nodes().contains(guess)) {
            System.out.println("Invalid Input, please enter a valid node name:");
            guess = scan.next();
            guess = reformat(guess);
        }
        return guess;
    }

    /**
     * Reformats the user's guess
     * to prevent case sensitivity issues
     *
     * @param guess user's guess
     * @return the reformatted guess
     */
    public static String reformat(String guess) {
        guess = guess.toLowerCase();
        char[] reformat = guess.toCharArray();
        reformat[0] = Character.toUpperCase(reformat[0]);
        return new String(reformat);
    }
}
