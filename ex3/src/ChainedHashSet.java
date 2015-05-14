import java.util.*;


public class ChainedHashSet extends SimpleHashSet{
	private int capacity;
	private int size;
	private final float upperLoadFactor;
	private final float lowerLoadFactor;
	private CollectionFacadeSet[] table;
	
	
	/**
	 * A default constructor. Constructs a new, empty table with default initial capacity
	 *  (16), upper load factor (0.75) and lower load factor (0.25).
	 */
	public ChainedHashSet(){
		this.initTable(CAPACITY_INIT);
		this.upperLoadFactor = UPPER_FACTOR_INIT;
		this.lowerLoadFactor = LOWER_FACTOR_INIT;
	}
	
	/**
	 * Constructs a new, empty table with the specified load factors, and the default 
	 * initial capacity (16).
	 * @param upperLoadFactor The upper load factor of the hash table.
	 * @param lowerLoadFactor The lower load factor of the hash table.
	 */
	public ChainedHashSet(float upperLoadFactor, float lowerLoadFactor){
		this.initTable(CAPACITY_INIT);
		this.upperLoadFactor = upperLoadFactor;
		this.lowerLoadFactor = lowerLoadFactor;
	}
	
	/**
	 * Data constructor - builds the hash set by adding the elements one by one. 
	 * Duplicate values should be ignored. The new table has the default values of 
	 * initial capacity (16), upper load factor (0.75), and lower load factor (0.25).
	 * @param data Values to add to the set.
	 */
	public ChainedHashSet(java.lang.String[] data){
		this.initTable(CAPACITY_INIT);
		this.upperLoadFactor = UPPER_FACTOR_INIT;
		this.lowerLoadFactor = LOWER_FACTOR_INIT;
		for(String str : data){
			this.add(str);
		}
	}
	
	
	@Override
	public boolean add(java.lang.String newValue){
		int hashValue = this.hash(newValue);
		//try to add to the current linked list
		if(!this.table[hashValue].add(newValue))
			return false;
		// value is new therefore it was added and we need to see if table-update is needed
		this.size++;
		if(this.loadFactor(this.size, this.capacity) > this.upperLoadFactor){
			//create new table and move everything there
			this.updateTable(this.capacity * REFACTOR_SIZE);
		}
		return true;
	}

	@Override
	public boolean contains(String searchVal) {
		int hashValue = this.hash(searchVal);
		return this.table[hashValue].contains(searchVal);
	}

	@Override
	public boolean delete(String toDelete) {
		int hashValue = this.hash(toDelete);
		// try to delete if existing
		if(!this.table[hashValue].delete(toDelete))
			return false;
		// the value was inside therefore we need to update.
		this.size--;
		if(this.loadFactor(this.size, this.capacity) < this.lowerLoadFactor){
			//create new table and move everything there
			this.updateTable(this.capacity / REFACTOR_SIZE);
		}
		return true;
			
	}

	@Override
	public int size() {
		return this.size;

	}

	@Override
	public int capacity() {
		return this.capacity;
	}
	
	/*
	 * This function updates the table if it was resized according to the new size given. 
	 * @param newCapacity the new table size to create.
	 */
	private void updateTable(int newCapacity){
		if (newCapacity < LOWEST_CAPACITY)
			return;
		CollectionFacadeSet[] oldTable = this.table;
		this.initTable(newCapacity);
		for(CollectionFacadeSet col : oldTable){
			String[] tempArr = col.toArray();
			for(String str: tempArr){
				this.add(str);
			}
		}
	}
	
	/*
	 * calculates the hash values and returns a position in the table
	 */
	private int hash(String newValue){
		int hashValue = newValue.hashCode();
		int tableSizeMinusOne = this.capacity - 1;
		return hashValue & tableSizeMinusOne;
	}
	
	/*
	 * initializes new table of CollectionFacadeSet to act as an array of linked lists.
	 * it updates the number of elements to 0, and the capacity to the new value. 
	 */
	private void initTable(int capacity){
		if (capacity < LOWEST_CAPACITY)
			return;
		CollectionFacadeSet[] newTable = new CollectionFacadeSet[capacity];
		for(int i = 0; i< capacity; i++){
			newTable[i] = new CollectionFacadeSet(new LinkedList<String>());
		}
		this.table = newTable;
		this.capacity = capacity;
		this.size = 0;
	}
	
	/*
	 * the function calculates the loadFactor with the 2 numbers provided.
	 */
	private float loadFactor(int num1, int num2){
		if(num2 == 0)
			return -1;
		return (((float)num1)/(float)num2);
	}

}
