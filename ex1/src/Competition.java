import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, consisting of 
 * a given number of rounds. It also keeps track of the number of victories of each 
 * player.
 * @author guybrush
 */
public class Competition {
	
	// i chose to put those variables here instead of the Player class because i would have 
	// needed a public method to get it each time and it was not allowed.
	private static final int PLAYER1_ID = 1;
	private static final int PLAYER2_ID = 2;
	private static final int NUM_OF_PLAYERS = 2;
	
	private int[] pScores = new int[NUM_OF_PLAYERS];
	private boolean msgFlag;
	
	private final Player player1;
	private final Player player2;
	
	/**
	 * Receives two Player objects, representing the two competing opponents, and a flag 
	 * determining whether messages should be displayed.
	 * @param player1 The Player objects representing the first player.
	 * @param player2 The Player objects representing the second player.
	 * @param displayMessage - a flag indicating whether gameplay messages should be 
	 * printed to the console. 
	 */
	public Competition(Player nimPlayer1, Player nimPlayer2, boolean displayMessage){
		for (int i=0; i<pScores.length; i++)
			pScores[i] = 0;
		player1 = nimPlayer1;
		player2 = nimPlayer2;
		msgFlag = displayMessage;
	}
	
	/**
	 * If playerPosition = 1, the results of the first player is returned. 
	 * If playerPosition = 2, the result of the second player is returned. 
	 * If playerPosition equals neiter, -1 is returned.
	 * @param playerPosition
	 * @return then number of victories of a player.
	 */
	public int getPlayerScore(int playerPosition){
		int score;
		if(playerPosition <= pScores.length)
			score = pScores[playerPosition-1];
		else
			score = -1;

		return score;
	}
	
	/*
	 * Returns the integer representing the type of the player; returns -1 on bad input.
	 */
	private static int parsePlayerType(String[] args,int index){
		try{
			return Integer.parseInt(args[index]);
		} catch (Exception E){
			return -1;
		}
	}
	
	/*
	 * Returns the integer representing the number of games; returns -1 on bad input.
	 */
	private static int parseNumberOfGames(String[] args){
		try{
			return Integer.parseInt(args[2]);
		} catch (Exception E){
			return -1;
		}
	}

	/*
	 * this method performs one turn for a player.
	 * @param player the player to make his turn.
	 */
	private void playTurn(int id, Board board){
		Player playerPlaying;
		if (id  == PLAYER1_ID){
			playerPlaying = player1;
		}
		else{
			playerPlaying = player2;
		}
		if(msgFlag)
			System.out.println("Player " + playerPlaying.getPlayerId() + 
																", it is now your turn!");
		Move move = playerPlaying.produceMove(board);
		while(board.markStickSequence(move) != 1){
			if(msgFlag) 
			System.out.println("Invalid move. Enter another:");
			move = playerPlaying.produceMove(board);
		}
		if(msgFlag)
			System.out.println("Player " + playerPlaying.getPlayerId() + 
														" made the move: "+move.toString());
	}
	
	/*
	 * plays one round until win of a player.
	 */
	private int playRound(){
		Board board = new Board();
		if(msgFlag)
			System.out.println("Welcome to the sticks game!");
		int curPlayer = PLAYER1_ID; 							// player1 always goes first
		while(checkIfGameEnd(board) != true){
			playTurn(curPlayer, board);
			curPlayer = curPlayer % NUM_OF_PLAYERS + 1;
		}
		/* if we got here, then the last player did the last move and he lost! he took the
		* last stick on the board otherwise curPlayer would change.
		*
		*returns the 1 player before because he won.
		*/
			return curPlayer;
	}
	
	/**
	 * Run the game for the given number of rounds.
	 * @param numberOfRounds - number of rounds to play.
	 */
	public void playMultipleRounds(int numberOfRounds){
		// always displays at the start of competition
		System.out.println("Starting a Nim competition of " + numberOfRounds + 
				" rounds between a " + player1.getTypeName() + " player and a " +
				player2.getTypeName() + " player.");
		
		while (numberOfRounds > 0){
			int winner = playRound();
			pScores[winner-1] ++;
			numberOfRounds--;
			if(msgFlag)
				System.out.println("Player " + winner + " won!");
		}
		/* always displays at the end of the competition, assumes 2 players, will need to be
		 * slightly changed to accommodate more players.
		 */
		System.out.println("The results are " + getPlayerScore(1) + ":" + getPlayerScore(2));
	}
	
	/*
	 * compares whether there are any sticks left on the board to mark. If none are left 
	 * than the game ends.
	 * @param board the game board to check.
	 * @return true if no stick are left to mark, false otherwise.
	 */
	private boolean checkIfGameEnd (Board board){
		if(board.getNumberOfUnmarkedSticks() == 0)
			return true;
		return false;
	}
	
	/**
	 * The method runs a Nim competition between two players according to the three 
	 * user-specified arguments. 
	 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for 
	 * a Random computer player, 2 for a Heuristic computer player, 3 for a Smart computer 
	 * player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input arguments, as 
	 * detailed above.
	 */
	public static void main(String[] args) {
		
		int p1Type = parsePlayerType(args,0);
		int p2Type = parsePlayerType(args,1);
		int numGames = parseNumberOfGames(args);
		
		/* You need to implement this method */
		Scanner scanner = new Scanner(System.in);
		Player nimPlayer1 = new Player(p1Type, PLAYER1_ID, scanner);
		Player nimPlayer2 = new Player(p2Type, PLAYER2_ID, scanner);
		
	
		boolean msgConditionResult = msgCondition(p1Type, p2Type);
		
		Competition nimGame = new Competition(nimPlayer1, nimPlayer2, msgConditionResult);
		
		nimGame.playMultipleRounds(numGames);
	}	
	
	/*
	 * checks if one of the player types are human so as to show or not messages in 
	 * "verbose mode".
	 * @param p1Type the player 1 type.
	 * @param p2Type the player 2 type.
	 * @return true if either of the player is human, false otherwise.
	 */
	private static boolean msgCondition(int p1Type, int p2Type){
		if (p1Type == Player.HUMAN || p2Type == Player.HUMAN)	
			return true;
		return false;
	}
	
}
