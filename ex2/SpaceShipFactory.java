/**
 * This class implements the factory, which creates an array of new ships according to
 * the arguments given.
 * 
 * @author guybrush
 */

public class SpaceShipFactory {
	/*
	 * Constants according to exercise documents.
	 */
	private static final String HUMAN_CASE = "h";
	private static final String BASHER_CASE = "b";
	private static final String RUNNER_CASE = "r";
	private static final String AGGRESSIVE_CASE = "a";
	private static final String DRUNK_CASE = "d";
	private static final String SPECIAL_CASE = "s";
	
	/**
	 * This function creates an array of spaceships 
	 * @param args the arguments according to which to build the array
	 * @return array of spaceships or null if argument not valid 
	 */
    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] array = new SpaceShip[args.length];
        for(int i=0; i<args.length; i++){
        	switch(args[i]){
        	case HUMAN_CASE:
    			array[i] = new SpaceShipHuman();
    			break;
        	case RUNNER_CASE:
    			array[i] = new SpaceShipRunner();
    			break;
        	case BASHER_CASE:
    			array[i] = new SpaceShipBasher();
    			break;
        	case AGGRESSIVE_CASE:
    			array[i] = new SpaceShipAggressive();
    			break;
        	case DRUNK_CASE:
    			array[i] = new SpaceShipDrunk();
    			break;
        	case SPECIAL_CASE:
    			array[i] = new SpaceShipSpecial();
    			break;
    		default:
    			return null;
        	}
        		
        }
    	return array;
    }
}
