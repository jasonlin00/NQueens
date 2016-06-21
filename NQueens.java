
import java.util.ArrayList;
import java.util.Random;

public class NQueens {

	public static void main(String args[]) {
		generateTestCases();
	}
	
	
	
	
	private static void generateTestCases() {
		
		Random rand = new Random();
		int numTestCases = 100;
		int boardLength = 18;
		int counter = 0;
		int searchCounter = 0;

		long startTime = 0;
		long endTime = 0;
		
		System.out.printf("Running %d test cases:\n", numTestCases);
		startTime = System.nanoTime();
		for(int i = 0; i < numTestCases; i++) {
			
			int[] board = new int[boardLength];
			for(int j = 0; j < boardLength; j++) {
				board[j] = rand.nextInt(boardLength);
			}
			Node n = new Node(board);
			int[] result = hillClimbingSearch(board);
			
			if(result[0] == 0)
				counter++;
			searchCounter += result[1];
			
		}
		endTime = System.nanoTime();
		System.out.printf("Hill Climbing successful cases: %d\n", counter);
		System.out.printf("Hill Climbing average search cost: %d\n", searchCounter / numTestCases);
		System.out.printf("Hill Climbing average time cost: %fms\n", (endTime - startTime)/  1e8);
		
		counter = 0;
		searchCounter = 0;

		startTime = 0;
		endTime = 0;
		
		System.out.printf("Running %d test cases:\n", numTestCases);
		startTime = System.nanoTime();
		for(int i = 0; i < numTestCases; i++) {
			
			int[] board = new int[boardLength];
			for(int j = 0; j < boardLength; j++) {
				board[j] = rand.nextInt(boardLength);
			}
			Node n = new Node(board);
			int[] result = minConflicts(board);
			
			if(result[0] == 0)
				counter++;
			searchCounter += result[1];
			
		}
		endTime = System.nanoTime();
		System.out.printf("Minimum Conflicts Algorithm successful cases: %d\n", counter);
		System.out.printf("Minimum Conflicts Algorithm average search cost: %d\n", searchCounter / numTestCases);
		System.out.printf("Minimum Conflicts Algorithm average time cost: %fms\n", (endTime - startTime)/  1e8);
	}



	//performs the steepest ascent hill climbing search on a board state
	// returns int array of size 2 with first int representing number of conflicts remaining and the second representing search cost
	public static int[] hillClimbingSearch(int[] board) {
		
		Node n = new Node(board);
		int successors = 0;
		int counter = 0;
		
		while(true) {
			ArrayList<Node> children = n.generateChildren();
			successors += children.size();
			int min = children.get(0).getConflicts();
			int minIndex = 0;
			for(int i = 1; i < children.size(); i++) {
				if(children.get(i).getConflicts() <= min) {
					min = children.get(i).getConflicts();
					minIndex = i;
				}
			}
			
			if(min == 0) 
				return new int[]{0, successors};
			
			
			if(min >= n.getConflicts())
				return new int[]{n.getConflicts(), successors};
			
			n = children.get(minIndex);
		}
	}
	
	

	//performs the minimum conflicts algorithm on a board state
	// returns int array of size 2 with first int representing number of conflicts remaining and the second representing search cost
	public static int[] minConflicts(int[] board) {
		
            int moves = 0;
            Random random = new Random();
            ArrayList<Integer> tempList = new ArrayList<Integer>();

            while (true) {
            	
                int maxConflicts = 0;
                for (int i = 0; i < board.length; i++) {
                    int conflicts = conflicts(board, board[i], i);
                    if (conflicts == maxConflicts) {
                        tempList.add(i);
                    } else if (conflicts > maxConflicts) {
                        maxConflicts = conflicts;
                        tempList.clear();
                        tempList.add(i);
                    }
                }

                if (maxConflicts == 0) 
                    return new int[]{0, moves};

                int worstQueenColumn = tempList.get(random.nextInt(tempList.size()));
                
                int minConflicts = board.length;
                tempList.clear();
                
                for (int i = 0; i < board.length; i++) {
                    int conflicts = conflicts(board, i, worstQueenColumn);
                    if (conflicts == minConflicts) {
                        tempList.add(i);
                    } else if (conflicts < minConflicts) {
                        minConflicts = conflicts;
                        tempList.clear();
                        tempList.add(i);
                    }
                }

                if (!tempList.isEmpty()) 
                    board[worstQueenColumn] = tempList.get(random.nextInt(tempList.size()));
                

                moves++;
                //cap off moves to 300 to ensure it is not looping infinitely
                if(moves == 300) {
                	Node n = new Node(board);
                	return new int[]{n.getConflicts(), moves};
                }
            }
        
	}
	
	
	//get number of conflicts of a specific queen at row and column
	public static int conflicts(int[] board, int row, int col) {
		int count = 0;
        for (int c = 0; c < board.length; c++) {
            if (c == col) continue;
            int r = board[c];
            if (r == row || Math.abs(r-row) == Math.abs(c-col)) count++;
        }
        return count;
	}
	
	
	public static class Node {
		private int conflicts;
		private int[] board;
		
		public Node() {
			
		}
		
		public Node(int[] board) {
			this.board = board;
			this.conflicts = conflicts(this.board);
		}
		
		public ArrayList<Node> generateChildren() {
			
			ArrayList<Node> childrenNodes = new ArrayList<Node>();
			
			for(int i = 0; i < board.length; i++) {
				for(int z = 0; z < board.length; z++) {
				int[] temp = new int[board.length];
				for(int j = 0; j < board.length;j++) {
					temp[j] = board[j];
				}
				if(z != temp[i])
					temp[i] = z;
				
				Node n = new Node(temp);
				childrenNodes.add(n);
				}
			}
			
			
			return childrenNodes;
		}
		
		//get total number of conflicts in board
		public static int conflicts(int[] board) {
			int conflicts = 0;
			int difference = 0;
			
			for(int i = 0; i < board.length; i++) {
				for(int j = i + 1; j < board.length; j++) {
					
					if(board[i] == board[j])
						conflicts++;
					
					difference = j - i;
					
					if(board[i] == board[j] - difference || board[i] == board[j] + difference)
						conflicts++;
		
				}		
			}
			
			return conflicts;
		}

		
		// Print the current State
		public void printState() {
			for(int row = 0; row < board.length; row++) {
				for(int col = 0; col < board.length; col++) {
					if(board[col] ==  row)
						System.out.print(" Q ");
					else
						System.out.print(" X ");
					
				}
				System.out.println();
			}
			
			System.out.println("Number of conflicts: " + conflicts + "\n");
		}
		
		public int getConflicts() {
			return conflicts;
		}

		public void setConflicts(int conflicts) {
			this.conflicts = conflicts;
		}
	}
}
