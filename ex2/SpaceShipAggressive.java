/**
 * This class implements a Aggressive space ship which tries to shot others.
 * extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipAggressive extends SpaceShip{
	private SpaceShip closest;
	private double distToClose;
	private double angleToClose;
	
	/**
	 * Aggressive ship constructor
	 */
	public SpaceShipAggressive(){
		this.reset();
	}
	
	/**
	 * This method decides the aggressive action
	 * @param game the game engine to use
	 */
	public void doAction(SpaceWars game){
		this.roundUpdate();
		// aggressive always accelerates while we reset 
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
		
		// tries to shot if close
		if (distToClose <= THREAT_RADIUS && 
				Math.abs(angleToClose) <= THREAT_RADIUS){
			this.fire(game);
		}
		this.getPhysics().move(this.accl, this.turn);
	}
}
