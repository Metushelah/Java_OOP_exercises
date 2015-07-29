package oop.ex6.logic_analysis;

import oop.ex6.logic_analysis.methods.Method;
import oop.ex6.logic_analysis.variables.Variable;

/**
 * Represents the main scope of a Sjava program. It's a singelton.
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
     * creates a new Main scope. Mainly used for the presubmit testing.
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
     * Throws UndeclaredMethodCallException if an attempt to call an undeclared method occured
     * (method was not found in methods).
     * Throws BadMethodCallException if @arguments were bad.
     * @param methodName the method we try to call.
     * @param arguments the arguments we provide to the method.
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

    //todo is this needed? check with text_analysis and scope.
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
     * @return a Variable array containing all the unassigned variables in MainScope.
     */
    Variable[] getUnassignedGlobals() {
        return unassignedGlobals;
    }

    //todo strange documentation. probably not in place?
    /** 
     * Attempts to declare the new @method.
     * Returns the scope of the new method.
     * (This functions as openScope()! It is very important that it does so, otherwise we will have API
     * problems with the decorated Scope!)
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
