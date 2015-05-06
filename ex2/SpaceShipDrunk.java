import java.util.Random;

/**
 * This class implements a Drunk space ship which acts wierdly.
 * extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipDrunk extends SpaceShip {
	private Random randGen = new Random();
	
	/**
	 * the drunk ship constructor
	 */
	public SpaceShipDrunk(){
		this.reset();
	}
	
	/**
	 * The method drunk pilot uses to decide it's action.
	 * @param game the game engine
	 */
	public void doAction(SpaceWars game){
		this.roundUpdate();
		int randAction = randGen.nextInt(10);
		
		//chooses the random action
		switch(randAction){
		case(0):
			this.teleport();
		case(1):
			this.accl = true;
		case(2):
			this.fire(game);
		case(3):
			this.turn = RIGHT_TURN;
		case(4):
			this.shieldOn();
			break;
		default:
			this.turn = LEFT_TURN;
			break;
		}
		this.getPhysics().move(this.accl, this.turn);
	}
}
