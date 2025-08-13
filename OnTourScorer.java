import java.io.*;
import java.util.*;

public class OnTourScorer {

    public static int[] vals; // Array to the space number of each space
    public static int[] pts; // Array to store points of each space
    public static List<List<Integer>> neighs; // Adjacency list to store list of neighbors for each space
    public static int maxScore = 0; //Stores final max score we will write to output file
    public static List<Integer> bestPath = new ArrayList<>(); //ArrayList we will iterate through to write the path taken to get the max score

    public static void main(String[] args) throws IOException {
    	
        // Read from input file
        Scanner sc = new Scanner(new File("example-input.txt")); //Change file name based on input file you want to test
        int n = sc.nextInt(); // Stores number of spaces in our game

        vals = new int[n + 1]; //Initialize vals array with 1-based indexing
        pts = new int[n + 1]; //Initialize pts arrays with 1-based indexing
        neighs = new ArrayList<>(); //Initialize the adjacency list for each space
        
        // Each index i in 'neighs' will store a list of neighbors for space i, so an empty list is stored in each index
        for (int i = 0; i <= n; i++) {
            neighs.add(new ArrayList<>());
        }

        // Read and store each space's value, points, and neighbors in the corresponding arrays/arrayList
        for (int i = 1; i <= n; i++) {
            vals[i] = sc.nextInt();         // v(i): value of the space
            pts[i] = sc.nextInt();          // p(i): points assigned to the space
            int numNeighbors = sc.nextInt();   // number of neighbors
            for (int j = 0; j < numNeighbors; j++) { //iterate through all neighbors for the current space
                int nbID = sc.nextInt();      // neighbor ID stores the current neighbor's index value
                if (nbID >= 1 && nbID <= n) {
                    neighs.get(i).add(nbID);  // Add the current neighbor ID to the list of neighbors for space i
                }
            }
        }

        // Function will compute the max score and the corresponding best path
        iterativePath(n);

        // Write results to output file
        try (PrintWriter writer = new PrintWriter("example-output.txt")) {
            writer.println(maxScore); // First line: the max score
            for (int i = 0; i < bestPath.size(); i++) {
                writer.print(vals[bestPath.get(i)] + " "); // Iterate through bestPath and print the space values with one extra space at the end
            }
        } 
        System.out.println("Max Score: " + maxScore);
        System.out.print("Path: ");
        for (int i = 0; i < bestPath.size(); i++) {
            System.out.print(+ vals[bestPath.get(i)] + " "); // Iterate through bestPath and print the space values
        }
        sc.close(); //close scanner
    }

    /**
     * Computes the maximum score and the corresponding path traversed to get the max score.
     * A node can only extend a path from a neighbor with a smaller value, and adjacent neighbors cannot
     * have the same space values, although space values can be repeated in the game. 
     */
    public static void iterativePath(int n) {
        /*
         * Create an array of space ID's. This is necessary so that we can later on check the neighbors of all 
         * spaces including duplicate space values. The array is then sorted in ascending order based on the corresponding 
         * space values. This is critical to ensure our dynamic programming loop can compare the current space to all smaller neighbors
         */
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i + 1;
        }
        Arrays.sort(order, Comparator.comparingInt(i -> vals[i]));

        int[] dp = new int[n + 1]; // dp[i]: array will store the max score possible of the path traversed to the current node
        int[] pred = new int[n + 1]; // pred[i]: array stores the id of the space visited before the current space

        //Iterate through the space ID's which are in ascending sorted order in terms of v[i]
        for (int i : order) {
            dp[i] = pts[i]; // Stores the current spaces point's as it's total score in case there are no neighbors
            for (int j : neighs.get(i)) { //executes if the current space has neighbors
                if (vals[j] < vals[i]) { // Only consider neighbors with smaller values
                    int candidate = dp[j] + pts[i]; //stores the total score calculated for the current space 
                    if (candidate > dp[i]) { //if the total score for the current space is greater than the score in dp[i]
                        dp[i] = candidate; //update dp[i] to the candidate score  
                        pred[i] = j; // update pred to store the id of the previous space with the greatest score calculated so far in the traversal
                    }
                }
            }
        }

        // Iterate and compare the score of all n spaces to find the best score calculated
        int end = 1;
        for (int i = 2; i <= n; i++) {
            if (dp[i] > dp[end]) {
                end = i;
            }
        }

        maxScore = dp[end]; // Update maxScore to the best score calculated

        // Reconstruct the best scoring path by backtracking through the predecessor array.
        // Starting from the end node, follow each recorded predecessor and add it to the path.
        while (end != 0) {
            bestPath.add(end);
            end = pred[end];
        }

        /*
         * Spaces were added to bestPath from the end to start. 
         * Reverse bestPath so we can write the path from start to end in the output file
         */
        Collections.reverse(bestPath);
    }
}
