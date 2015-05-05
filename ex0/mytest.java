/*line 100 my couse div by zero probloem in the max_afford method,to avoid fix the method or delte the line,
there is no define instrcutions to max_afford when the price is 0.*/

class mytest{
    public static void main(String[] args){
        ProductType apple = new ProductType("apple",1,2);
        ProductType orange = new ProductType("orange",2,4); 
        ProductType kiwi = new ProductType("kiwi",4,4);                
        ProductType shoe = new ProductType("shoe",0,0);                
        ProductType nails = new ProductType("nails",2,5);               
        ProductType hammer = new ProductType("hammer",2,9);                               
        Store my_store = new Store();
        my_store.addProductType(apple);
        my_store.addProductType(apple);        
        System.out.println(my_store.stringRepresentation()+"\n");
        /*
        Store has a balance of 0, and the following products:
        [apple,1,2]
        [apple,1,2]       
        */
        Customer my_customer = new Customer("dotan", "dorot", 500);
        my_store.makePurchase(my_customer,"apple",5);
        System.out.println(my_customer.getPurchaseLog()+"\n");
        /*
        Shopping log for customer: dotan
        5 apple
        */        
        my_store.makePurchase(my_customer,"apple",4);
        System.out.println(my_customer.getPurchaseLog()+"\n");
        /*
        Shopping log for customer: dotan
        5 apple
        4 apple
        */                   
        System.out.println(my_store.stringRepresentation()+"\n");
        //Store has a balance of 9, and the following products:
        //[apple,1,2]
        //[apple,1,2]
        System.out.println(my_customer.balance+"\n"); 
        //482
        System.out.println(my_store.sellsProductsOfType("apple")+"\n");  
        //true
        System.out.println(my_store.sellsProductsOfType("orange")+"\n");  
        //false
        my_store.addProductType(orange); 
        System.out.println(my_store.sellsProductsOfType("orange")+"\n");  
        //true
        System.out.println(my_store.stringRepresentation()+"\n");   
        /*
        Store has a balance of 9, and the following products:
        [apple,1,2]
        [apple,1,2]    
        [orange,2,4]
        */          
        my_store.addProductType(kiwi);        
        my_store.addProductType(shoe);        
        my_store.addProductType(nails);       
        System.out.println(my_store.addProductType(hammer)+"\n"); 
        //false 
        System.out.println(my_store.stringRepresentation()+"\n");   
         /*
        Store has a balance of 9, and the following products:
        [apple,1,2]
        [apple,1,2]        
        [orange,2,4]
        [kiwi,4,4]
        [shoe,0,0]
        */     
        System.out.println(my_customer.balance+"\n"); 
        //482
        my_store.makePurchase(my_customer,"kiwi",400);
        System.out.println(my_customer.getPurchaseLog()+"\n");  
        /*
        Shopping log for customer: dotan
        5 apple
        4 apple
        120 kiwi
        */ 
        System.out.println(my_customer.balance+"\n");  
        //2
        Customer my_customer2 = new Customer("dotanir", "dorot", -500);
        my_store.makePurchase(my_customer2,"apple",4);                 
        System.out.println(my_customer2.getPurchaseLog()+"\n");  
        //Shopping log for customer: dotanir
        System.out.println(my_customer2.balance + "\n"); 
        //-500      
        System.out.println(my_store.removeProductTypeFromStore("kiwi")); 
        //true
        System.out.println(my_store.removeProductTypeFromStore("mango"));   
        //false
        System.out.println(my_store.stringRepresentation()+"\n");  
        /*
        Store has a balance of 9, and the following products:
        [apple,1,2]
        [apple,1,2]
        [orange,2,4]
        [shoe,0,0]
        */
        System.out.println(my_store.sellsProductsOfType("kiwi"));   
        //false
        System.out.println(my_store.makePurchase(my_customer2,"shoe",4));  
        //0
        System.out.println(my_store.makePurchase(my_customer2,"orange",-4)); 
        //0

        
    }
}        


/* 
#i had a problem of starting a new line after addition to the logs,if you 
see in the cmd more than one space between lines it could cause mastakes...just saying
# compering to primiteves (espacially null) with .equal could couse problomes.
thanks to Natanel Ficher on a correction*/

