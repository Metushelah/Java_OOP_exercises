import oop.ex2.*;
import java.awt.Image;

/**
 * This class implements a Human controlled space ship which extends SpaceShip.
 * 
 * @author guybrush
 */
public class SpaceShipHuman extends SpaceShip{
	private final Image shipShldOff = GameGUI.SPACESHIP_IMAGE;
	private final Image shipShldOn = GameGUI.SPACESHIP_IMAGE_SHIELD;
	
	/**
	 * The constructor for the human ship
	 */
	public SpaceShipHuman(){
		this.reset();
	}
	
	/**
	 * This method updates and executes the actions a human ship can perform.
	 * @param game is the game engine of type SpaceWars
	 */
	public void doAction(SpaceWars game){
		// the first method called by each ship to reset shield and update cooldown and move
		this.roundUpdate();
		this.accl = false;
		//tries to teleport
		if (game.getGUI().isTeleportPressed())
			this.teleport();
		// tries to move
		if (game.getGUI().isUpPressed())
			this.accl = true;
		if (game.getGUI().isLeftPressed())
			this.turn = LEFT_TURN;
		if (game.getGUI().isRightPressed())
			this.turn = RIGHT_TURN;
		this.getPhysics().move(this.accl, this.turn);
		// tries to turn on shields
		if (game.getGUI().isShieldsPressed())
			this.shieldOn();
		// tries to shot
		if (game.getGUI().isShotPressed())
			this.fire(game);
	}
	
	/**
	 * Overrides the getImage() method in SpaceShip because human image is different 
	 */
	public Image getImage(){
        if (this.shieldStatus())
        	return this.shipShldOn;
        return this.shipShldOff;
	}
}
