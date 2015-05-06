/**
 * This class implements a Runner space ship which tries to escape all the time.
 * extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipRunner extends SpaceShip {
	
	private SpaceShip closest;
	private double distToClose;
	private double angleToClose;
	
	/**
	 * the constructor for the runner ship
	 */
	public SpaceShipRunner(){
		this.reset();
	}
	
	/**
	 * The method runner ship uses to decide on an action.
	 * @param game the game engine
	 */
	public void doAction(SpaceWars game){
		this.roundUpdate();
		// runner always accelerates while we reset 
		this.accl = true;
		closest = game.getClosestShipTo(this);
		distToClose = closest.getPhysics().distanceFrom(this.getPhysics());
		angleToClose = closest.getPhysics().angleTo(this.getPhysics());
		
		//decide turning angle
		if (angleToClose < 0)
			this.turn = RIGHT_TURN;
		else if (angleToClose > 0)
			this.turn = LEFT_TURN;
		else
			this.turn = NO_TURN;
		
		// tries to teleport and run away
		if (distToClose <= THREAT_RADIUS && 
				Math.abs(angleToClose) <= THREAT_RADIUS){
			this.teleport();
		}
		this.getPhysics().move(this.accl, this.turn);
	}
}
