
import java.util.*;

/**
 * Wraps an underlying Collection and serves to both simplify its API and give it a 
 * common type with the implemented SimpleHashSets.
 * @author guybrush
 */
public class CollectionFacadeSet implements SimpleSet{
	
	private Collection <String> collection;
	
	/**
	 * Creates a new facade wrapping the specified collection.
	 * @param collection The Collection to wrap.
	 */
	public CollectionFacadeSet(java.util.Collection<java.lang.String> collection){
		this.collection = collection;
	}

	@Override
	public boolean add(String newValue) {
		if (newValue == null || collection.contains(newValue))
			return false;
		return collection.add(newValue);
		
	}

	@Override
	public boolean contains(String searchVal) {
		if (searchVal == null)
			return false;
		return collection.contains(searchVal);
	}

	@Override
	public boolean delete(String toDelete) {
		if (toDelete == null)
			return false;
		return collection.remove(toDelete);
	}

	@Override
	public int size() {
		return collection.size();
	}
	
	/**
	 * This function returns an array of Strings from the collection. It returns an array
	 * of Strings because we were told we will work with Strings.
	 * @return Array of Strings from the collection.
	 */
	public String[] toArray(){
		return this.collection.toArray(new String[0]);
	}
}
