/**
* This class represents a type of product that can be added to a store,
* and purchased by a customer. The product has a name, a store price
* (how much it costs a store to purchase this product) and a customer price
* (the price for which stores seel this product to customers).
* @author guybrush
*/
class ProductType {
	
	/**
	* The name of this type of products.
	*/
	String name;
	/**
	* How much a store pays per product of this type.
	*/
	int storePrice;
	/**
	* How much a store pays per product of this type.
	*/
	int customerPrice;

	/**
	* Constructs a new type of product with the given parameters.
	* @param productName - The name of the product.
	* @param productStorePrice - How much the product costs a store.
	* @param productCustomerPrice - How much the product costs a customer.
	*/	
	ProductType (String productName, int productStorePrice, 
				int	productCustomerPrice){
		if (productName != null) name = productName;
		else name = "";
		if (productStorePrice >= 0) storePrice = productStorePrice;
		else storePrice = 0;
		if (productCustomerPrice >= 0) customerPrice = productCustomerPrice;
		else customerPrice = 0;
	}

	/**
	* The profit per unit this product type, calculated with the formula: 
	* profit = (customer price) - (store price)
	* @return The profit per unit for products of this type.
	*/
	int profitPerUnit(){
		return customerPrice  - storePrice;
	}

	/**
	* Returns a string representation of the product type, which is a sequence 
	* of the product name, store price and customer price, separated by 
	* commas, inclosed in square brackets. For example, if the product is 
	* called "Apple", costs "50" to a store and "80" to a customer, this 
	* method will return the string: "[Apple,50,80]"
	* @return the String representation of this ProductType object.
	*/
	String stringRepresentation(){
		char OPEN_CHAR = '[';
		char CLOSE_CHAR = ']';        
		return OPEN_CHAR + name + "," + storePrice + "," +
				customerPrice +	CLOSE_CHAR;
	}

}
