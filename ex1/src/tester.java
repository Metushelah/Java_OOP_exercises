public class tester {
	public static void main(String[] args){
		System.out.println("Beging Testing");
		System.out.println("");
		System.out.println("Move Class Testing");
		Move move1 = new Move(2,3,5);
		System.out.println(move1.toString());
		System.out.println(move1.getRow());
		System.out.println(move1.getLeftBound());
		System.out.println(move1.getRightBound());
		
		//Player player = new Player(1,1,null);
		Board board = new Board();
		//int[][] array;
		//array = player.produceSmartMove(board);
		//System.out.println(array.length);
		//print(array);
		move1 = new Move(1,3,3);
		board.markStickSequence(move1);
		move1 = new Move(1,6,6);
		board.markStickSequence(move1);
		//print(player.produceSmartMove(board));
		
	}
	public static void print(int[][] array){
		for(int[] i:array){
			for(int j: i)
				System.out.println(j);
		}
	}

}