import oop.ex2.*;

/**
 * This class implements a Special space ship which HiJacks the GUI and creates doubles
 * and changes text to DEATH!!. It mainly acts as the aggressive type spaceship.
 * extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipSpecial extends SpaceShip {
	private SpaceShip closest;
	private double distToClose;
	private double angleToClose;
	
	/**
	 * Special ship constructor
	 */
	public SpaceShipSpecial(){
		this.reset();
	}
	
	/**
	 * This method decides the action for special ship
	 * @param game the game engine to use
	 */
	public void doAction(SpaceWars game){
		this.roundUpdate();
		closest = game.getClosestShipTo(this);
		distToClose = closest.getPhysics().distanceFrom(this.getPhysics());
		angleToClose = this.getPhysics().angleTo(closest.getPhysics());
		
		//decide turning angle
		if (angleToClose < 0)
			this.turn = RIGHT_TURN;
		else if (angleToClose > 0)
			this.turn = LEFT_TURN;
		else
			this.turn = NO_TURN;
		
		// tries to shot if close and creates doubles and shouts DEATH!
		if (distToClose <= THREAT_RADIUS && 
				Math.abs(angleToClose) <= THREAT_RADIUS){
			this.fire(game);
			oop.ex2.SpaceShipPhysics pos1 = new SpaceShipPhysics();
			oop.ex2.SpaceShipPhysics pos2 = new SpaceShipPhysics();
			game.getGUI().addImageToBuffer(this.getImage(), pos1);
			game.getGUI().addImageToBuffer(this.getImage(), pos2);
			game.getGUI().setText("DEATH DEATH DEATH !!! ");
		}
		this.getPhysics().move(this.accl, this.turn);
	}
}
