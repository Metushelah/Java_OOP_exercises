package oop.ex6.logic_analysis.scope;

import oop.ex6.logic_analysis.methods.*;
import oop.ex6.logic_analysis.methods.IllegalArgumentException;
import oop.ex6.logic_analysis.variables.Variable;

import java.util.*;

/**
 * Represents the main scope of a Sjava program. It's a singleton.
 * 
 * Created by orrp and guybrush on 6/15/15.
 */
public class MainScope extends Scope {
    private static MainScope instance;

    // Variable map exists in extended scope
    private final Map<String, Method> methods;
    // This is updated once parsing the Main is done.
    private Variable[] unassignedGlobals;

    
    private MainScope(){
        methods = new HashMap<>();
    }

    /**
     * creates a new Main scope. Mainly used for the pre-submit testing.
     * @return a MainScope instance.
     */
    public static MainScope init(){
        instance = new MainScope();
        return instance;
    }

    /**
     * @return the MainScope current instance.
     */
    public static MainScope getInstance(){
        // Is not null because of init()
        return instance;
    }

    /**
     * Attempts to call a method named @methodName, with the provided @arguments.
     * Throws UndeclaredMethodCallException if an attempt to call an undeclared method occurred
     * (method was not found in methods).
     * @param methodName the method we try to call.
     * @param arguments the arguments we provide to the method.
     * @throws UndeclaredMethodCallException when calling on a method not found.
     * @throws IllegalArgumentException when providing bad arguments to the method.
     */
    public void callMethod(String methodName, Queue<Variable> arguments) throws
            							UndeclaredMethodCallException, IllegalArgumentException {
        Method method = methods.get(methodName);
        if (method == null){
            throw new UndeclaredMethodCallException();
        }
        if (!method.isCallValid(arguments)){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retains the unassignedGlobals in the MainScope.
     */
    public void updateUnassignedGlobals(){
        ArrayList<Variable> unassignedVariables = new ArrayList<>();
        for (Variable variable : getVariables()){
            if (!variable.isAssigned()){
                unassignedVariables.add(variable);
            }
        }
        unassignedGlobals = unassignedVariables.toArray(new Variable[unassignedVariables.size()]);
    }


    /**
     * Operation not supported by the MainScope. Use declareMethodBody() instead
     */
    public Scope declareSubScope(){
        throw new UnsupportedOperationException("Operation not supported. Use declareMethod()");
    }

    /**
     * @return a Scope from the method's body.
     */
    public Scope declareMethodBody(Method method){
        return new Scope(method);
    }

    /**
     * tries to add a method to the MainScope's method list.
     * @param method a method to add.
     * @throws MethodAlreadyDeclaredException when trying to declare a method with the same name.
     */
    public void declareMethod(Method method) throws MethodAlreadyDeclaredException {
        if (methods.put(method.getName(), method) != null){
            throw new MethodAlreadyDeclaredException();
        }
    }

    /**
     * This method is useful for dealing with unassigned globals that might be assigned in method.
     * It reverts back all changes made inside a method to unassigned globals. 
     */
    public void revertGlobals(){
        for (Variable global : unassignedGlobals){
            global.unassign();
        }
    }

    /**
     * @return the methods in MainScope.
     */
    public Collection<Method> getMethods(){
        return methods.values();
    }

}
