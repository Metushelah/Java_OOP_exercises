package oop.ex6.logic_analysis;

import oop.ex6.logic_analysis.methods.Method;
import oop.ex6.logic_analysis.scope.MainScope;
import oop.ex6.logic_analysis.scope.UndeclaredAssignmentException;
import oop.ex6.logic_analysis.scope.VariableAlreadyDeclaredException;
import oop.ex6.logic_analysis.variables.IllegalValueException;
import oop.ex6.logic_analysis.variables.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a scope in our Sjava syntax like a scope inside a method or an if/while condition.
 * 
 * Created by orrp and guybrush on 6/15/15.
 */
public class Scope {

    private final Map<String, Variable> variables;
    private final Scope parent;

    /**
     * Shouldn't be used, apart from the MainScope construction.
     */
    Scope(){
        variables = new HashMap<>();
        parent = null;
    }

    /**
     * Constructs a scope and gives it it's parent to remember.
     * @param parent a Scope which came before this one.
     */
    Scope(Scope parent){
        variables = new HashMap<>();
        this.parent = parent;
    }

    //todo do we use this at all in the new way we implemented the scopehack? fix documentation.
    /**
     * This constructor is used for opening the scope of a new method.
     * It deals with pathological cases such as parameters and unassigned globals of the same name.
     * [That's why it's so ugly]
     * @param method a Method for which the new scope is opened.
     */
    Scope(Method method){
        Variable[] parameters = method.getParameters();
        this.variables = new HashMap<>();
        if (parameters != null){ // May be null, if method had no parameters
            for (Variable parameter : parameters){
                this.variables.put(parameter.getName(), parameter);
            }
        }
        parent = MainScope.getInstance();
    }


    /**
     * Opens a new subScope (if/while scopes, for this class) and returns a reference to it.
     * @return a Scope with the calling scope as his parent. 
     */
    public Scope declareSubScope(){
        return new Scope(this);
    }

    /**
     * Attempts to add the variable to the Scope Variable reference. If already exists
     * (i.e put didn't return null), Throws VariableAlreadyDeclaredException.
     * @param variable a Variable type which to add to this scope.
     */
    public void declareVariable(Variable variable) throws VariableAlreadyDeclaredException {
        if (variables.put(variable.getName(), variable) != null){
            throw new VariableAlreadyDeclaredException();
        }
    }

    /**
     * attempts to assign a value to an existing variable.
     * @param name the variable name to search.
     * @param value a String representing the value we want to assign.
     */
    public void assignVariable(String name, String value) throws UndeclaredAssignmentException,
            IllegalValueException {
        Variable variable = findVariable(name);
        if (variable == null){
            throw new UndeclaredAssignmentException();
        }
        variable.assignValue(value);
    }

    /**
     * @return this scopes parent scope.
     */
    public Scope getParent() {
        return parent;
    }

    /**
     * searches for a variable in the local scope and upwards till MainScope.
     * @param name a String with the variables name.
     * @return a Variable or null if variable not found 
     */
    public Variable findVariable(String name){
        Variable variable;
        Scope cur = this;
        do{
            variable = cur.variables.get(name);
            cur = cur.getParent();
        }while (cur != null && variable == null);
        return variable;
    }

    //todo do we still need this method? if so add documentation.
    /**
     * This method is used by MainScope. See @MainScope.updateUnassignedGlobals();
     * It seems rather wasteful to first add them to a list, and the toArray() that list, but this
     * method is only called once per run.
     */
    Variable[] findUnassignedVariables(){
        ArrayList<Variable> unassignedVariables = new ArrayList<>();
        for (Variable variable : variables.values()){
            if (!variable.isAssigned()){
                unassignedVariables.add(variable);
            }
        }
        return unassignedVariables.toArray(new Variable[unassignedVariables.size()] );
    }

    //todo do we need this aswell? 
    /**
     * @return a Collection representing the local scope Variables.
     */
    public Collection<Variable> getVariables() {
        return variables.values();
    }
}
