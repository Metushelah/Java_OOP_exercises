import java.awt.Image;
import oop.ex2.*;

/**
 * The API spaceships need to implement for the SpaceWars game. 
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 *  a base class for the other spaceships or any other option you will choose.
 *  
 * @author guybrush
 */
public abstract class SpaceShip{
	/**
	 * Constants specified in SpaceShipPhysics for move method turn variables.
	 * @param NO_TURN will hold 0;
	 * @param RIGHT_TURN will hold -1;
	 * @param LEFT_TURN will hold 1;
	 */
	protected static final int NO_TURN = 0;
	protected static final int RIGHT_TURN = -1;
	protected static final int LEFT_TURN = 1;
	
	/**
	 * A constant to which we compare the threats. Specified as 0.2 in documents.
	 * @param THREAT_RADIUS the radius specified in the ex2 documents
	 */
	protected static final double THREAT_RADIUS = 0.2;
	
	/*
	 * Constants specified in the ex2 documents special for every ship.
	 */	
	private static final int MAX_HEALTH = 20;
	private static final int MIN_HEALTH = 0;
	private static final int BASE_MAX_ENERGY = 200;
	private static final int HIT_DAMAGE = 1; // The damage the ship receives being hit
	private static final int ENERGY_BASH_REUPTAKE = 20; // Energy replenished when bashing
	private static final int ENERGY_LOST = 10; // When hit with shields down
	private static final int ENERGY_PER_TURN_RECHARGE = 1; //Energy recharged in current energy pool
	private static final int SHOT_COST = 20;
	private static final int TELEPORT_COST = 150;
	private static final int SHIELD_UP_COST = 3;
	private static final int SHOT_COOLDOWN = 8;
	
	private final Image shipShldOff = GameGUI.ENEMY_SPACESHIP_IMAGE;
	private final Image shipShldOn = GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
	
	/*
	 * variables that will change in the game
	 */
	private int curHealth, curEnergy, nextShotCountDown, maxEnergy;
	private boolean shieldUp;
	private oop.ex2.SpaceShipPhysics position;
	
	/**
	 * This are both protected because we will be using them from the subclasses and i didn't
	 * want to create special getters and setters for them.
	 * @param turn will hold the direction the spaceship need to turn
	 * @param accl will hold if the spaceship needs to accelerate of not
	 */
	protected int turn;
	protected boolean accl;
	
   
    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * Will be special for each subShip created
     * 
     * @param game the game object to which this ship belongs.
     */
    public abstract void doAction(SpaceWars game);

    /**
     * This method is called every time a collision with this ship occurs 
     */
    public void collidedWithAnotherShip(){
    	if(this.shieldUp){
    		this.maxEnergy += ENERGY_BASH_REUPTAKE;
    		this.curEnergy += ENERGY_BASH_REUPTAKE;
    	}
    	else
    		this.gotHit();
    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
    	this.maxEnergy = BASE_MAX_ENERGY;
    	this.curEnergy = BASE_MAX_ENERGY;
    	this.curHealth  = MAX_HEALTH;
    	this.nextShotCountDown = 0;
    	this.position = new oop.ex2.SpaceShipPhysics();
    }

    /**
     * Checks if this ship is dead.
     * 
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
    	if (this.curHealth <= MIN_HEALTH)
    			return true;
        return false;
    }

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return this.position;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot. It is also called by Collision Method if shields are down.
     */
    public void gotHit() {
    	// hit if shields are up or down, i need to implement this.
    	if (!this.shieldUp){
    		this.curHealth -= HIT_DAMAGE;
    		this.maxEnergy -= ENERGY_LOST;
    		if (this.curEnergy > this.maxEnergy) 
    			this.curEnergy = this.maxEnergy;
    	}
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * 
     * @return the image of this ship.
     */
    public Image getImage(){
        if (this.shieldUp)
        	return this.shipShldOn;
        return this.shipShldOff;
    }

    /**
     * Attempts to fire a shot.
     * 
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
    	if (game == null)
    		return;
        if(this.curEnergy > SHOT_COST && this.nextShotCountDown <= 0){
    	    this.nextShotCountDown = SHOT_COOLDOWN;
    	    this.curEnergy -= SHOT_COST;
    	    game.addShot(this.getPhysics());
       }
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
    	if (this.curEnergy >= SHIELD_UP_COST)
    		this.shieldUp = true;
    		this.curEnergy -= SHIELD_UP_COST;
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
       if (this.curEnergy >= TELEPORT_COST){	   
    	   this.curEnergy -= TELEPORT_COST;
    	   this.position = new SpaceShipPhysics();
       }
    }
    
    /**
     * This method is called to update the energy each round and the shot count down.
     * called first in any ships action!!!
     */
    public void roundUpdate(){
    	this.shieldUp = false;
    	if (this.curEnergy < this.maxEnergy)
    		this.curEnergy += ENERGY_PER_TURN_RECHARGE;
    	this.nextShotCountDown --;
    	this.turn = NO_TURN;
    }
    
    /**
     * Returns the status of the shield
     * @return a boolean, true if shield is up and false otherwise.
     */
    public boolean shieldStatus(){
    	return this.shieldUp;
    }
    
}
