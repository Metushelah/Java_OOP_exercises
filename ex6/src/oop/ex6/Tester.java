package oop.ex6;

import oop.ex6.main.Sjavac;

/**
 * A testing class we created to go over the tests given with the exercise.
 * 
 * Created by orrp and guybrush on 6/21/15.
 */
public class Tester {

    private static final String DIR = "/home/orrp/uni/semester2/oop/ex6/from_school/tests/";
    private static final String PREFIX = "test";
    private static final String SUFFIX = ".sjava";

    /**
     * main programs that runs over the tests and runs the Sjava verifier over them.
     * @param args none given.
     */
    public static void main(String[] args){
        for (int d2 = 0; d2<6;d2++){
            for (int d1 = 0; d1<10;d1++){
                for (int d0 = 0; d0<10; d0++){
                    String num = String.valueOf(d2)+String.valueOf(d1)+String.valueOf(d0);
                    System.out.println("Test: "+num);
                    String[] args1 = {DIR+PREFIX+num+SUFFIX};
                    Sjavac.main(args1);
                }
            }
        }
        System.out.println("Done!");
    }


}
