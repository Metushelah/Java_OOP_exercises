package oop.ex6.logic_analysis.methods;

import oop.ex6.logic_analysis.variables.Variable;

import java.util.Queue;

/**
 * This class represents methods that can be implemented in Sjava. Such methods have a signature and
 * name, with a list of parameters and a method body
 * 
 * Created by orrp and guybrush on 6/15/15.
 */
public class Method {
    private final String name;
    private final Variable[] parameters;
    private final String[] body;

    /** main constructor for methods. We receive a Variable array because inside methods those count
     *  against other local variables in the scope.
     *  @param name a string with the method name.
     *  @param parameters a Variable array with the parameters from the signature.
     *  @param body a string array with the sentences from the body.
     */
    Method(String name, Variable[] parameters, String[] body){
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    /**
     *  Checks if a method call is compatible to method signature
     *  @param arguments the list of variables to compare to the method signature.
     *  @return True of False if the method call is according to signature.
     */
    public boolean isCallValid(Queue<Variable> arguments){
        // to deal with arguments and parameters of size 0, method without input.
        if (parameters == null){
            return arguments.size() == 0;
        }
        if (parameters.length != arguments.size()){
            return false;
        }
        // go over each argument and see if type matches.
        for (Variable parameter : parameters) {
            Variable curArg = arguments.poll();
            if (!curArg.isAssigned() ||
                    !curArg.getType().isA(parameter.getType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the body of the method.
     * @return String array holding the sentences in the body.
     */
    public String[] getBody() {
        return body;
    }

    /**
     * Returns the name of the method.
     * @return String holding the name of the method.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of parameters.
     * @return Variable array holding the parameters specified in the method signature.
     */
    public Variable[] getParameters() {
        return parameters;
    }
}
