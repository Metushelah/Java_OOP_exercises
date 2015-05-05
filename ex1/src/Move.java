/**
 * The Move class represents a move in the Nim game by a player. A move consists of the 
 * row on which it is applied, the left bound (inclusive) of the sequence of sticks to 
 * mark, and the right bound (inclusive) of the same sequence.
 * @author guybrush
 */

public class Move {
	
	private int row;
	private int leftBound;
	private int rightBound;
	
	/**
	 * Constructs a Move object with the given parameters
	 * @param inRow - The row on which the move is performed.
	 * @param inLeft - The left bound of the sequence to mark.
	 * @param inRight - The right bound of the sequence to mark.
	 */
	public Move(int inRow, int inLeft, int inRight){
		row = inRow;
		leftBound = inLeft;
		rightBound = inRight;
	}
	
	/**
	 * @overrides toString in class java.land.Object
	 * @return a string representation of the move. For example, if the row is 2, the left 
	 * bound of the sequence is 3 and the right bound is 5, this function will return the 
	 * string "2:3-5" (without any spaces).
	 */
	public java.lang.String toString(){
		return row + ":" + leftBound + "-" + rightBound;
	}
	
	/**
	 * @return The row on which the move is performed.
	 */
	public int getRow(){
		return row;
	}
	
	/**
	 * @return The left bound of the stick sequence to mark.
	 */
	public int getLeftBound(){
		return leftBound;
	}
	
	/**
	 * @return The right bound of the stick sequence to mark.
	 */
	public int getRightBound(){
		return rightBound;
	}
}
