
/**
 * This class implements a Basher space ship which tries to bash the other spaceships.
 * extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipBasher extends SpaceShip{
	private SpaceShip closest;
	private double distToClose;
	private double angleToClose;
	
	/**
	 * the Basher constructor
	 */
	public SpaceShipBasher(){
		this.reset();
	}
	
	/**
	 * this is the function basher uses to decide it's action
	 * @param game the game engine
	 */
	public void doAction(SpaceWars game){
		this.roundUpdate();
		// basher always accelerates while we reset 
		this.accl = true;
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
		
		// tries to shield and bash
		if (distToClose <= THREAT_RADIUS && 
				Math.abs(angleToClose) <= THREAT_RADIUS){
			this.shieldOn();
		}
		this.getPhysics().move(this.accl, this.turn);
	}
}
