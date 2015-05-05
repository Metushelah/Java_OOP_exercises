/**
* This class represents a customer that has an amount of money that can be 
* changed, and a purchase log documenting the purchases he performed.
* @author guybrush
*/
class Customer {
	
	/**
	* The name of this customer.
	*/
	String name;
	/**
	* The address of this customer's home.
	*/
	String address;
	/**
	* The amount of money this customer has.
	*/
	int balance;
	/**
	* A list of all of the successful purchases made by this customer.
	*/
	String log;

	/**
	* Construct a new customer with the given parameters.
	* @param customerName - The name of the customer.
	* @parm customerAddress - The address of the customer.
	* @parm customerBalance - The amount of money the customer has.
	*/
	Customer(String customerName, String customerAddress, int customerBalance){
		// Use the arguments of the constructor to initialize the fields
		// of this class.
		if (customerName != null) 
			name = customerName;
		else 
			name = "";
		if (customerAddress != null) 
			address = customerAddress;
		else 
			address = "";
		if (customerBalance >= 0)		
			balance = customerBalance;
		else
			balance = 0;
		log = "Shopping log for customer: " + name;
	}

	/**	
	* Return a string representation of this Customer, which is a sequence of 
	* the customer name, address and his balance, separated by commas, 
	* inclosed in square brackes. 
	*
	* For example, if the customer is called "Dana", lives in "Rotberg" and 
	* has 800 Shekels, this method will return the string: "[Dana,Rotberg,800]"
	* @return the String representation of this Customer object.
	*/
	String stringRepresentation(){
		char OPEN_CHAR = '[';
		char CLOSE_CHAR = ']';
		return OPEN_CHAR + name + "," + address + "," + balance + CLOSE_CHAR;
	}

	/**	
	* Checks whether this customer can afford to buy the given quantity of 
	* units of products of the given product type.
	* @param quantity - The number of units to check.
	* @param productType - The type of product to check.
	* @return true if this customer can afford to buy the given quantity of 
	* units of the given product type, false otherwise.
	*/
	boolean canAfford(int quantity, ProductType productType){
		if (quantity < 1 || productType == null) 
			return false;
		return productType.customerPrice * quantity <= balance;
	}

	/**
	* Calculates the maximal number of units of the given product type that 
	* this customer can afford to buy. 
	*
	* This can be zero if this customer cannot afford to buy even one unit of 
	* this product type.
	* @param productType - The type of product to make the calculation for.
	* @return the maximal number of units of the given product that this 
	* customer can afford to buy.
	*/
	int maximumAffordableQuantity(ProductType productType){
		if (productType != null)
			return balance / productType.customerPrice;
		return 0;
	}
	
	/**
	* @return the purchase log of this customer.
	*/
	String getPurchaseLog(){
		return log;
	}

	/**
	* Makes a purchase for this customer of the given amount of product of the 
	* given type, if he can afford it; this includes taking the price of the 
	* purchase out of his balance and adding the purchase to the purchase log 
	* of the customer. Non-positive quantities are ignored.
	* @param quantity - The number of products to purcahse.
	* @param productType - The type of products to purchase.
	*/
	void makePurchase(int quantity, ProductType productType){
		if (quantity > 0 && productType != null){
			if (canAfford(quantity, productType)){
				balance -= 	quantity * productType.customerPrice;
				log  = log + "\n" + quantity + " " + productType.name;
			}
		}
	}
	
}
