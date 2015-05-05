
/**
* This class represents a store. The store can sell up to five types of 
* products at any time. A store starts with a balance of 0 money, and it earns 
* money as purchases are made in the store by customers.
* @author guybrush
*/
class Store {
	
	/**
	* The maximal number of types of products this store can hold.
	* @see Constant Field Values
	*/
	final int MAX_NUM_OF_PRODUCT_TYPES = 5;
	/**
	* The amount of money this store has.
	*/
	int balance;
	/**
	* An array of ProductType objects, containing the type of products this 
	* store sells.
	*/		
	ProductType[] productTypeArray;

	/**
	* Constructs a new Store object. The constructor recieves no parameters, 
	* rather only initializing any internal variables this class uses.
	*/	
	Store() {
		balance = 0;
		productTypeArray = new ProductType[MAX_NUM_OF_PRODUCT_TYPES];
	}

	/**
	* Return a string representation of the store, which includes a sentence 
	* detailing the store's balance, and an additional line for each product 
	* in the store, matching the string representation of the corresponding 
	* product. For example, a store with a balance of 190 NIS and two products 
	* - Tomatoes and Shoes - will have the following string representation:\n 
	* Store has a balance of 190, and the following products:\n
	* [Tomato,2,5]\n
	* [Shoes,80,130]
	* @return the String representation of this Store object.
	*/
	String stringRepresentation(){
		String finalRepresentation = "Store has a balance of " + balance + 
											", and the following products:";

		for(int i =0; i < productTypeArray.length; i++){
			if (productTypeArray[i] != null)
				finalRepresentation = finalRepresentation + "\n" +
									productTypeArray[i].stringRepresentation();
		}
		
		return finalRepresentation;
	}

	/**
	* Attempts to add a type of product to the store. If succeeded retuns 
	* true, else return false.
	* @param productType - The type of product to add to the store.
	* @return true if the product was added, false otherwise.
	*/
	boolean addProductType(ProductType productType){
		if (productType == null)
			return false;		
		
		int i = 0;
		while (i != productTypeArray.length){
			if (productTypeArray[i] == null){
				productTypeArray[i] = new ProductType(productType.name, 
							productType.storePrice, productType.customerPrice);
				return true;
			}
			i++;
		}
		return false;
	}

	/**
	* finds a product's position in the array if exists
	* @param productTypeName - the product to find in the Array
	* @return -1 if not found or the position in the array
	*/
	private int findProductPositionInArray(String productTypeName){
		if (productTypeName == null)
			return -1;

		for (int i=0; i<productTypeArray.length; i++){
			if (productTypeArray[i] != null && 
				productTypeArray[i].name == productTypeName)
					return i;
		}
		return -1;
	}

	/**
	* Checks whether this store sells products of the given name.
	* @param productTypeName - The product name to check for.
	* @return true if products with the given name are sold by store, false 
	* otherwise.
	*/
	boolean sellsProductsOfType(String productTypeName){
		if(findProductPositionInArray(productTypeName) != -1)
			return true;
		return false;
	}

	/**
	* Removes a product type with the given name from the store. If more than 
	* one product type with this name is in the store, only one product is 
	* removed; all other product types with the same name will not be removed. 
	* If no product type with the given name is in the store, nothing happens.
	* @param productTypeName - The name of the product type to remove.
	* @return true if such a product type was in the store and was removed 
	* successfully, false if no such product type was removed.
	*/	
	boolean removeProductTypeFromStore(String productTypeName){
		int productPosition = findProductPositionInArray(productTypeName);
		if (productPosition != -1){
			productTypeArray[productPosition] = null;
			return true;
		}
		return false;
	}

	/**
	* Make a purchase for the given customer of the given amount of products 
	* of the given type, if he can afford it, and if the store sells product 
	* of this type; this includes updating the balance of the store with the 
	* profit it made from this purchase. If the customer cannot afford this 
	* purchase, a purchase for the maximal number of products he can afford is 
	* made.
	* @param customer - The customer that wishes to make the purchase.
	* @param productTypeName - The name of the type of product the customer 
	* wishes to purchase.
	* @param quantity - The number of products of the given type the customer 
	* wishes to purchase.
	* @return The actual number of products purchased of the product.
	*/
	int makePurchase(Customer customer, String productTypeName, int quantity){
		int productPosition = findProductPositionInArray(productTypeName);
		if(customer == null || productTypeName == null || quantity < 1 ||
														productPosition == -1)
			return 0;
		
		if (!customer.canAfford(quantity, productTypeArray[productPosition])){
			quantity = customer.maximumAffordableQuantity(
										productTypeArray[productPosition]);
		}
		customer.makePurchase(quantity, productTypeArray[productPosition]);
		balance += quantity * 
							productTypeArray[productPosition].profitPerUnit();
		return quantity;
	}

}
