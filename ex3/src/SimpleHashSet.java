
/**
 * A superclass for implementations of hash-sets implementing the SimpleSet interface.
 * @author guybrush
 *
 */
public abstract class SimpleHashSet implements SimpleSet {	
	protected static final int CAPACITY_INIT = 16;
	protected static final int REFACTOR_SIZE = 2;
	protected static final float LOWEST_CAPACITY = 1;
	protected static final float LOWER_FACTOR_INIT = 0.25f;
	protected static final float UPPER_FACTOR_INIT = 0.75f;
	
	/**
	 * @return The current capacity (number of cells) of the table.
	 */
	public abstract int capacity();
		
}
