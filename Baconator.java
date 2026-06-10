import java.util.ArrayList;
import java.util.Scanner;

public class Baconator extends DWGraph {
    static int points = 9;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DWGraph graph = new DWGraph("src\\16-node-sample.json");
        System.out.println("Welcome to Baconator\nEnter number to pick a game mode:\n" +
                "0)Guess Mode\n" + "1)Gamble Mode");
        int mode = checkInvalidInput(scan);
        printNodes(mode, graph, scan);


    }

    public static String[] getPaths(DWGraph graph) {
        String src = randomNodes(graph);
        String dest = randomNodes(graph);
        String output = graph.search(src, dest);
        return graph.parsePath(output);
    }

    public static String randomNodes(DWGraph graph) {
        ArrayList<String> nodes = graph.nodes();
        int rand = (int) (Math.random() * nodes.size());
        return nodes.get(rand);
    }

    public static int checkInvalidInput(Scanner scan) {
        String input = scan.next();
        if (!input.equals("0") && !input.equals("1")) {
            System.out.println("Invalid Input, Please re-enter game mode:");
            checkInvalidInput(scan);
        }
        int inputInt = Integer.parseInt(input);
        if (inputInt != 0 && inputInt != 1) {
            System.out.println("Invalid Input, Please re-enter game mode:");
            checkInvalidInput(scan);
        }

        return inputInt;
    }

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

    public static String userGuess(DWGraph graph, String[] path, Scanner scan) {
        System.out.println("Enter a node name to check if it is between the two nodes:");
        String guess = scan.next();
        guess = reformat(guess);
        System.out.println(guess);
        if ((guess.equals("\n") || guess.equals(" ") && path.length == 2)) {
            points++;
            return "Correct, there is a direct link";
        }
        while (!graph.nodes().contains(guess)) {
            System.out.println("Invalid Input, please enter a valid node name:");
            guess = scan.next();
            guess = reformat(guess);
        }
        return guess;
    }

    public static String reformat(String guess) {
        guess = guess.toLowerCase();
        char[] reformat = guess.toCharArray();
        reformat[0] = Character.toUpperCase(reformat[0]);
        return new String(reformat);
    }
}
