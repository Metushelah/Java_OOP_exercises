public class OpenHashSet extends SimpleHashSet{
	private static final String FLAG = new String("FLAG");
	private int capacity;
	private int size;
	private final float upperLoadFactor;
	private final float lowerLoadFactor;
	public String[] table;
	
	/**
	 * A default constructor. Constructs a new, empty table with default initial capacity
	 *  (16), upper load factor (0.75) and lower load factor (0.25).
	 */
	public OpenHashSet(){
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
	public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
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
	public OpenHashSet(java.lang.String[] data){
		this.initTable(CAPACITY_INIT);
		this.upperLoadFactor = UPPER_FACTOR_INIT;
		this.lowerLoadFactor = LOWER_FACTOR_INIT;
		for(String str : data){
			this.add(str);
		}
	}

	
	@Override
	public boolean add(String newValue) {
		if (newValue == null || newValue == FLAG)
			return false;
		
		int iteration = 0;
		int hashValue = this.hash(newValue, iteration);
		while(table[hashValue]!=null && table[hashValue]!= FLAG && !(table[hashValue].equals(newValue)))
			hashValue = this.hash(newValue, ++iteration);
		
		// the value is either a null or FLAG or equivalent, so we check if it is in already. 
		if(table[hashValue]!=null && table[hashValue].equals(newValue))
			return false;
		// value is new therefore it will be added and we need to see if table-update is needed.
		this.table[hashValue] = newValue;
		this.size++;
		if(this.loadFactor(this.size, this.capacity) > this.upperLoadFactor){
			//create new table and move everything there
			this.updateTable(this.capacity * REFACTOR_SIZE);
		}
		return true;
	}

	@Override
	public boolean contains(String searchVal) {
		if(searchVal == null)
			return false;
		
		int iteration = 0;
		int hashValue = this.hash(searchVal, iteration);
		while(table[hashValue]!=null && table[hashValue]!= FLAG && !table[hashValue].equals(searchVal))
			hashValue = this.hash(searchVal, ++iteration);
		
		// the value is either a null or FLAG or equivalent, so we check if it is in already.
		if(table[hashValue]!= null && table[hashValue].equals(searchVal))
			return true;
		return false;
	}

	@Override
	public boolean delete(String toDelete) {
		if(toDelete == null)
			return false;
		
		int iteration = 0;
		int hashValue = this.hash(toDelete, iteration);
		while(table[hashValue]!=null && table[hashValue]!= FLAG && !table[hashValue].equals(toDelete))
			hashValue = this.hash(toDelete, ++iteration);
		
		// the value is either a null or FLAG or equivalent, so we check if it is in already.
		if(table[hashValue] != null && table[hashValue].equals(toDelete)){
			table[hashValue] = FLAG;
			this.size--;
			if(this.loadFactor(this.size, this.capacity) < this.lowerLoadFactor){
				//create new table and move everything there
				this.updateTable(this.capacity / REFACTOR_SIZE);
			}
			return true;
		}
		return false;

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
		String[] oldTable = this.table;
		this.initTable(newCapacity);
		for(String str : oldTable){
			this.add(str);
		}
	}
	
	/*
	 * initializes new table of CollectionFacadeSet to act as an array of linked lists.
	 * it updates the number of elements to 0, and the capacity to the new value. 
	 */
	private void initTable(int capacity){
		if (capacity < LOWEST_CAPACITY)
			return;
		String[] newTable = new String[capacity];
		this.table = newTable;
		this.capacity = capacity;
		this.size = 0;
	}
	
	/*
	 * calculates the hash values and returns a position in the table
	 */
	private int hash(String newValue, int iterNum){
		int hashValue = (newValue.hashCode() + ((iterNum + 1)* iterNum)/ REFACTOR_SIZE);
		int tableSizeMinusOne = this.capacity - 1;
		return hashValue & tableSizeMinusOne;
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
