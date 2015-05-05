import java.util.Random;
import java.util.Scanner;

/**
 * The Player class represents a player in the Nim game, producing Moves as a response to 
 * a Board state. Each player is initialized with a type, either human or one of several 
 * computer strategies, which defines the move he produces when given a board in some 
 * state. The heuristic strategy of the player is already implemented. You are required 
 * to implement the rest of the player types according to the exercise description.
 * @author OOP course staff and me, guybrush Threepwood, Mighty Pirate!
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	
	//Used by produceHeuristicMove() for binary representation of board rows.
	private static final int BINARY_LENGTH = 3;
	
	private final int playerType;
	private final int playerId;
	private Scanner scanner;
	private Random myRandom = new Random();
	
	/**
	 * Initializes a new player of the given type and the given id, and an initialized 
	 * scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type (we will see better ways to do this in the future).
		if (type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println("Received an unknown player type as a parameter"
					+ " in Player constructor. Terminating.");
			System.exit(-1);
		}		
		playerType = type;	
		playerId = id;
		scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return playerId;
	}
	
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(playerType){
			
			case RANDOM:
				return "Random";			    
	
			case SMART: 
				return "Smart";	
				
			case HEURISTIC:
				return "Heuristic";
				
			case HUMAN:			
				return "Human";
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return "UnkownPlayerType";
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. The player 
	 * is given the board object, and is required to return his next move on the board. 
	 * The choice of the move depends on the type of the player: a human player chooses his 
	 * move manually; the random player should return some random move; the Smart player 
	 * can represent any reasonable strategy; the Heuristic player uses a strong heuristic 
	 * to choose a move.
	 * @param board - a Board object representing the current state of the game.
	 * @return a Move object representing the move that the current player will play 
	 * according to his strategy.
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);				
				    
			case SMART: 
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}
	
	
	/*
	 * get random left bound.
	 * @param board the board to check.
	 * @param row the row to look in.
	 * @return the random left bound or -1 if can't find any.
	 */
	private int getRandomLeftBound(Board board, int row ){
		int[] indexes = new int[board.getRowLength(row)];
		int numberInArray = 0;
		for (int i=1; i <= board.getRowLength(row) ; i++){
			if(board.isStickUnmarked(row, i)){
				indexes[i-1]= 1;
				numberInArray++;
			}
		}
		if (numberInArray==0)
			return -1;
		else {
			int index = myRandom.nextInt(indexes.length);	
			while(indexes[index]!=1)
				index = myRandom.nextInt(indexes.length);
			return index + 1;
		}
	}
	/*
	 * this functions returns the maximal right bound you can take, given a left bound in a
	 * row.
	 * @param board the board to check in.
	 * @param row in what row we are looking for.
	 * @param left the leftist bound which is unmarked. 
	 * @return index with the right bound.
	 */
	private int maxRightBound(Board board, int row, int left){
		int right = left;
		while(board.isStickUnmarked(row, right+1))
			right++;
		return right;
	}
	/*
	 * Produces a random move.
	 */
	private Move produceRandomMove(Board board){
		int randRow;
		int leftB;
		int rightB;
		
		do {
			randRow = myRandom.nextInt(board.getNumberOfRows()) + 1;
		}while (getNumOfUnmarkedSticksInRow(board, randRow) == 0);
		
		leftB = getRandomLeftBound(board, randRow);
		
		rightB = maxRightBound(board, randRow, leftB);
		// now we randomize again what we found
		if (leftB != rightB){
			leftB = myRandom.nextInt(rightB - leftB) + leftB;
			if (leftB != rightB)
			rightB = myRandom.nextInt(rightB - leftB) + leftB;
		}
		
		Move move = new Move(randRow, leftB, rightB);
		
		return move;
	}
	
	
	/*
	 * get the number of unmarked sticks in a specific row. How many are left.
	 */
	private int getNumOfUnmarkedSticksInRow(Board board, int row){
		int count =0;
		for(int i=1; i<=board.getRowLength(row); i++){
			if(board.isStickUnmarked(row, i))
				count++;
		}
		return count;
	}
	/*
	 * counts and searches for the biggest group in the row. 
	 * returns the bounds of that biggest group.
	 */
	private int[] getMaxGroup(Board board, int row){
		int [] maxBounds = new int[2];
		int left = 0;
		int right = 0;
		for(int i=1; i <= board.getRowLength(row)+1; i++){
			if(board.isStickUnmarked(row, i)){
				if(left==0){
					left = i;
					right = left;
				}
				else
					right++;
			}
			else{
				if(maxBounds[0] == 0 ||right-left > maxBounds[1]-maxBounds[0]){
					maxBounds[0]=left;
					maxBounds[1]=right;
				}
				left = 0;
				right = 0;	
			}
		}
		return maxBounds;
	}
	/*
	 * Produce some intelligent strategy to produce a move
	 */
	private Move produceSmartMove(Board board){
		/*
		 * This array will hold the groups in our array in a 2D matrix.
		 * [arrays][group_size, row, first index]
		 */
		int activeRows = 0;
		int activeRowIndex =0;
		int unmarkedSticksInRow = 0;
		for(int row=1; row <= board.getNumberOfRows(); row++){
			if(getNumOfUnmarkedSticksInRow(board, row)>0){
				activeRows++;
				activeRowIndex = row;
				unmarkedSticksInRow = getNumOfUnmarkedSticksInRow(board, row);
			}
		}
		if(activeRows == 1){
			int[] maxGroup = getMaxGroup(board, activeRowIndex);
			if(board.getNumberOfUnmarkedSticks()==1)
				return new Move(activeRowIndex,maxGroup[0], maxGroup[1]);
			if(maxGroup[1]-maxGroup[0]+1 == unmarkedSticksInRow)
				return new Move(activeRowIndex, maxGroup[0], maxGroup[1]-1);
			else if(maxGroup[1]-maxGroup[0]+1 == unmarkedSticksInRow - 1)
				return new Move(activeRowIndex, maxGroup[0], maxGroup[1]);
		}
		return produceRandomMove(board);		
		}

	
	/*
	 * return the move the Human player makes by accepting input from him.
	 */
	private Move getHumanRowAndIndexes(){
		System.out.println("Enter the row number:");
		int row = scanner.nextInt();
		System.out.println("Enter the index of the leftmost stick:");
		int left = scanner.nextInt();
		System.out.println("Enter the index of the rightmost stick:");
		int right = scanner.nextInt();
		
		Move move = new Move(row, left, right);
		return move;
	}
	/*
	 * Interact with the user to produce his move.
	 */
	private Move produceHumanMove(Board board){
		final int DISPLAY_BOARD = 1;
		final int MAKE_MOVE = 2;
		int userInput;
		do{
			System.out.println("Press 1 to display the board. Press 2 to make a move:");
			userInput = scanner.nextInt();
			switch(userInput){
			case DISPLAY_BOARD:
				System.out.println(board.toString());
				break;
			case MAKE_MOVE:
				break;
			default:
				System.out.println("Unknown input.");
				break;
			}
		}while(userInput != MAKE_MOVE);
		return getHumanRowAndIndexes();
	}
	
	
	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 */
	private Move produceHeuristicMove(Board board){

		if(board == null){
			return null;
		}
	
		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,
																															lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				//while(numOnes<eraseSize){
				while(numOnes<numRemove && i<board.getRowLength(eraseRow)){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				
				//This is the case that we cannot perform a smart move because there are marked
				//Sticks in the middle
				if(numOnes == numRemove){
					return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
				} else {
					return new Move(lastRow,lastLeft,lastLeft);
				}
				
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only need to erase a 
		//single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric,
		//and then we simply mark one stick from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}
	
	
}
