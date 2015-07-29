package oop.ex6.logic_analysis.methods;

import oop.ex6.logic_analysis.variables.Variable;

/**
 * The factory with which all other modules will interact to create and declare methods. A singleton.
 * 
 * Created by orrp and guybrush on 6/15/15.
 */
public class MethodFactory {

	// a singleton instance.
    private static MethodFactory instance;

    private MethodFactory(){}

    /**
     * @return the instance of the factory singleton.
     */
    public static MethodFactory getInstance() {
        if (instance == null) {
            instance = new MethodFactory();
        }
        return instance;
    }

    /**
     * generates a method from the arguments given. Calls on the method constructor.
     * @param name the name of the method.
     * @param parameters a Variable array holding the parameters from the signature.
     * @param body a string array holding the body of the method.
     */
    public Method generateMethod(String name, Variable[] parameters, String[] body){
        return new Method(name, parameters, body);
    }

}
