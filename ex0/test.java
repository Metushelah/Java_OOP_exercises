//comment
public class Test {

	static final Customer customerList[] = new Customer[10];
	static final Store storeList[] = new Store[10];
	static final ProductType productList[] = new ProductType[10];

	public static void main(String[] args) {
		init();
		System.out.println("init complete");
		test1();
		System.out.println("test1 complete");
		test2();
		System.out.println("test2 complete");
		test3();
		System.out.println("test3 complete");
		test4();
		System.out.println("test4 complete");
		test5();
		System.out.println("test5 complete");
		test6();
		System.out.println("test6 complete");
		System.out.println("all tests complete");
	}

	public static void init(){
		for (int i = 0; i < customerList.length; i++) {
			String tempName = "customer" + i;
			customerList[i] = new Customer(tempName, tempName + "address" + i,
					((i + 10) * 200));
		}

		for (int i = 0; i < productList.length; i++) {
			String tempName = "product" + i;
			productList[i] = new ProductType(tempName, ((i + 1) * 10),
					(int) Math.pow(((i + 1) * 10), 2));
		}

		for (int i = 0; i < storeList.length; i++) {
			storeList[i] = new Store();
			for (int j=i; j<productList.length; j++){
				boolean result = storeList[i].addProductType(productList[j]);
				if (j - i >= 5){
					if (result){
						System.out.println("Store init failed, expected false. " + i +", " + j);
					}
				} else if (!result) {
					System.out.println("Store init failed, expected true. " + i +", " + j);
				}
			}
		}
	}

	public static void test1(){
		// Test string representations for customers
		String[] expected = {
				"[customer0,customer0address0,2000]",
				"[customer1,customer1address1,2200]",
				"[customer2,customer2address2,2400]",
				"[customer3,customer3address3,2600]",
				"[customer4,customer4address4,2800]",
				"[customer5,customer5address5,3000]",
				"[customer6,customer6address6,3200]",
				"[customer7,customer7address7,3400]",
				"[customer8,customer8address8,3600]",
				"[customer9,customer9address9,3800]"
		};
		for (int i = 0; i < customerList.length; i++){
			String actual = customerList[i].stringRepresentation();
			if (!actual.equals(expected[i])){
				System.out.println("Missmatch. expected: " + expected[i] +", actual: " + actual);
			}
		}
	}


	public static void test2(){
		// Test string representations for customers
		String[] expected = {
				"[product0,10,100]",
				"[product1,20,400]",
				"[product2,30,900]",
				"[product3,40,1600]",
				"[product4,50,2500]",
				"[product5,60,3600]",
				"[product6,70,4900]",
				"[product7,80,6400]",
				"[product8,90,8100]",
				"[product9,100,10000]"
		};
		for (int i = 0; i < productList.length; i++){
			String actual = productList[i].stringRepresentation();
			if (!actual.equals(expected[i])){
				System.out.println("Missmatch. expected: " + expected[i] +", actual: " + actual);
			}
		}
	}

	public static void test3(){
		// Test string representations for customers
		String[] expected = {
				"Store has a balance of 0, and the following products:\n" +
						"[product0,10,100]\n" +
						"[product1,20,400]\n" +
						"[product2,30,900]\n" +
						"[product3,40,1600]\n" +
						"[product4,50,2500]",

				"Store has a balance of 0, and the following products:\n" +
						"[product1,20,400]\n" +
						"[product2,30,900]\n" +
						"[product3,40,1600]\n" +
						"[product4,50,2500]\n" +
						"[product5,60,3600]",

				"Store has a balance of 0, and the following products:\n" +
						"[product2,30,900]\n" +
						"[product3,40,1600]\n" +
						"[product4,50,2500]\n" +
						"[product5,60,3600]\n" +
						"[product6,70,4900]",

				"Store has a balance of 0, and the following products:\n" +
						"[product3,40,1600]\n" +
						"[product4,50,2500]\n" +
						"[product5,60,3600]\n" +
						"[product6,70,4900]\n" +
						"[product7,80,6400]",

				"Store has a balance of 0, and the following products:\n" +
						"[product4,50,2500]\n" +
						"[product5,60,3600]\n" +
						"[product6,70,4900]\n" +
						"[product7,80,6400]\n" +
						"[product8,90,8100]",

				"Store has a balance of 0, and the following products:\n" +
						"[product5,60,3600]\n" +
						"[product6,70,4900]\n" +
						"[product7,80,6400]\n" +
						"[product8,90,8100]\n" +
						"[product9,100,10000]",

				"Store has a balance of 0, and the following products:\n" +
						"[product6,70,4900]\n" +
						"[product7,80,6400]\n" +
						"[product8,90,8100]\n" +
						"[product9,100,10000]",

				"Store has a balance of 0, and the following products:\n" +
						"[product7,80,6400]\n" +
						"[product8,90,8100]\n" +
						"[product9,100,10000]",

				"Store has a balance of 0, and the following products:\n" +
						"[product8,90,8100]\n" +
						"[product9,100,10000]",

				"Store has a balance of 0, and the following products:\n" +
						"[product9,100,10000]"

		};
		for (int i = 0; i < storeList.length; i++){
			String actual = storeList[i].stringRepresentation();
			if (!actual.equals(expected[i])){
				System.out.println("Missmatch. expected:\n" + escape(expected[i]) +"\nactual:\n" + escape(actual));
			}
		}
	}
	
	public static String escape(String string){
	    return string.replace("\r", "\\r").replace("\n", "\\n\n");
	}
	
	public static void test4(){
	    Store emptyStock = new Store();
	    boolean actual = emptyStock.removeProductTypeFromStore("Cool Product");
	    if (actual) {
	        System.out.println("test4 failed: an item was removed from an " +
	                "empty store");
	    }
	}
	
	public static void test5(){
	    Customer customer = new Customer("Jhon Doe", "4.5'th ave.", 10);
	    String expected = "Shopping log for customer: Jhon Doe";
	    String actual = customer.getPurchaseLog();
	    if (!expected.equals(actual)){
	        System.out.println("test5 failed: customer didn't buy a thing," +
	                " expected log:\n" + expected + "\nactual:\n" + actual);
	    }
	}
	
	public static void test6(){
	    Store fullStock = new Store();
	    fullStock.addProductType(new ProductType("Mango_Lard_Juice", 4, 9));
	    fullStock.addProductType(new ProductType("Camel.Milk", 23, 33));
	    fullStock.addProductType(new ProductType("3HamsterToez!!!", 120, 200));
	    fullStock.addProductType(new ProductType("toenails", 1, 2));
	    fullStock.addProductType(new ProductType("snails", 3, 4));
	    boolean actual = fullStock.removeProductTypeFromStore("Mango_Lard_Juice");
	    if (!actual) {
	        System.out.println("test6 failed: could not find an existing product");
	    }
	}
}

