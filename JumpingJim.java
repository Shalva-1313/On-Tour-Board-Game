import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

//By: Shasha Alvares
//CS 590 Project 3: Jumping Jim
//Due Date: 05/07/25

public class JumpingJim {
	
	//Initialize coordinate class to keep track of current row and column
	 public static class Coordinates{
		public final int row;
		public final int col;
		
		public Coordinates(int row, int col) {
			this.row=row;
			this.col=col;
		}
		
		//Redefine how two trampoline coordinates are equal since they are being used as keys in the hashmap
		@Override
		public boolean equals(Object object) {
		    if (this == object) {
		    	return true;
		    }
		    if (!(object instanceof Coordinates)) {
		    	return false;
		    }
		    Coordinates coord = (Coordinates) object;
		    return this.row == coord.row && this.col == coord.col;
		}

		//31 was prime number chosen to ensure each hash code is unique
		@Override
		public int hashCode() {
		    return 31 * row + col;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		int col; //keeps track of each col in the input file
		int row; //keeps track of each row in the input file 
		Queue<Coordinates> queue = new LinkedList<>(); //stores coordinates that need to be explored
		Set<Coordinates> visited = new HashSet<>(); //keeps track of visited coordinates so they are not re-explored
		Map<Coordinates,Coordinates> pathTracer = new HashMap<>(); //stores current trampoline and previous trampoline so path can be reconstructed
		Map<Coordinates, String> directionMap = new HashMap<>(); //used to reconstruct the N, S, E, W direction moved in

        Scanner scnr = new Scanner(new File("input.txt")); //change the name of the input file being evaluated here
        row = scnr.nextInt(); // stores the row value in the first line of the input file
        col = scnr.nextInt(); // stores the col value in the first line of the input file
        scnr.nextLine();//consume the rest of the line
		int[][] maze = new int[row][col]; //initialize 2D array that will store the value of each trampoline jump
        
        for(int i =0; i<row;i++) { //iterate through every row in the input file
        	for(int j=0; j<col; j++) { //iterate through every col in the input file 
        		maze[i][j] = scnr.nextInt(); //stores all jump values for each trampoline coordinate in the game board
        	}
        }

        //Set up start position for BFS traversal
        Coordinates startCoord = new Coordinates(0,0); //Jim's start trampoline position
		Coordinates goal = new Coordinates(row-1, col-1); //Jim's end trampoline position
        queue.add(startCoord); //adds starting coordinate to the queue
        visited.add(startCoord); //marks starting coordinate as visited so it will not be re-explored
        
        //BFS loop
        while(!queue.isEmpty()) { //traverse through the entire queue 
        	Coordinates current = queue.poll(); //get coordinates of the trampoline at the front of the queue
        	if (current.equals(goal)) {
                break; // If goal end trampoline is reached stop BFS
            }
        	int r = current.row;
        	int c = current.col;
        	int jump = maze[r][c]; //jumping value of current trampline Jim is exploring
        	
        	//jumping directions should be read as {possibleRowMoves[0], possibleColMoves[0]}
        	int[] possibleRowMoves = {-1, 1, 0, 0}; //N, S, E, W
        	int[] possibleColMoves = {0, 0, 1, -1}; //N, S, E, W
    		String[] directions = {"N", "S", "E", "W"};

        	
        	//explore which of the 4 possible jumps are possible
        	for(int i=0; i<4;i++) {
        		int nextRow = r + possibleRowMoves[i]*jump;
        		int nextCol = c + possibleColMoves[i]*jump;
        		
        		//if jump is within gameboard bounds (between 0-(row-1) and 0 to (col-1)), add to queue
        		if(nextRow >=0 && nextRow < row && nextCol>=0 && nextCol < col)
        		{
        			Coordinates nextNode = new Coordinates(nextRow, nextCol); 
        			if(!visited.contains(nextNode)){ //if the nextNode has not yet been visited add to queue
        				queue.add(nextNode);	//add new trampoline coordinate to queue
        				visited.add(nextNode);	// mark new trampoline coordinate as visited so we don't revisit once evaluated
        				pathTracer.put(nextNode, current); //add nextNode and current to hashmap so we can trace path
        				directionMap.put(nextNode, directions[i]); //track direction we moved in	
        			}
        		}
        	}
        }
        
        //Goal has been reached - now reconstruct the path from end trampoline to start trampoline
        Coordinates currNode = new Coordinates(row-1, col-1);
        List<String> path = new ArrayList<>(); //use to reconstruct path taken in terms of N, S, E, W rather than coordinates
        
        while(pathTracer.containsKey(currNode)) {        	
        	path.add(directionMap.get(currNode));
        	currNode = pathTracer.get(currNode);
        }
       
        //Write results to output file
        PrintWriter out = new PrintWriter(new File("output.txt"));
        Collections.reverse(path); //order is now start trampoline to end trampoline
        System.out.println();
        for (String d:path) {   //Iterate through the directions we constructed and print to outfile.txt the moves Jim made
            out.print(d + " "); //leads to an extra space at the end of the output which is okay for this assignment 
            System.out.print(d + " ");
        }
        out.close();
        scnr.close();
       }
}
