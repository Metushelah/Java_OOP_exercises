package oop.ex6.logic_analysis.variables;

import java.util.regex.Matcher;

/**
 * This class represents a Variable that we might get in Sjava syntax. Like a normal variable in 
 * Java it can be final, assigned a value, has a name and a type.
 *  
 * Created by orrp and guybrush on 6/15/15.
 */
public class Variable{
	
	/*
	 * each var will hold its type to compare to values and other variables assigned to it.
	 */
    private final VarType type; 
    /*
     * in this varifiar we don't really care for the value therefore assigned is a boolean.
     */
    private boolean isAssigned;
    private boolean isFinal;
    private final String name; 		

    /**
     * This is the only constructor that should be used when creating a variable. Checks if a 
     * Variable is final and assigned at the same time according to Sjava specifications.
     * @param isFinal whether the Variable is final or not.
     * @param VarType the type of the Variable according to the VarType enum.
     * @param name the Variable name.
     * @param value whether the Variable is assigned in declaration.
     */
    Variable(boolean isFinal, VarType type, String name, String value) throws
            										UnassignedFinalException, IllegalValueException {
        this.name = name;
        this.type = type;
        this.isFinal = false; // We start with false so that we may assignValue for the first time
        if (value != null){
            assignValue(value);
        }
        this.isFinal = isFinal;
        if (isFinal && !isAssigned){	// as per Sjava convention, final var means assigned.
            throw new UnassignedFinalException();
        }
    }

    /** 
     * Copy constructor. Used mainly for shadowing globals. Uses "deep copy" approach.
     * @param toCopy the Variable we wish to copy.
     */
    Variable(Variable toCopy){
        this.name = toCopy.name;
        this.type = toCopy.type;
        this.isFinal = toCopy.isFinal;
        this.isAssigned = toCopy.isAssigned();
    }


    /** 
     * Tries to assign a value to a variable. Checks if value is correct.
     * @param value the string with the value to assign.
     */
    public void assignValue(String value) throws IllegalValueException {
    	// checks if value provided fits the values that type can receive.
        Matcher matcher = type.getPattern().matcher(value);
        if (isFinal || !matcher.matches()){ 
            throw new IllegalValueException();
        }
        isAssigned = true;
    }

    /**
     * @return VarType enum representing type.
     */
    public VarType getType() {
        return type;
    }

    /**
     * @return True or False if the Variable is already assigned.
     */
    public boolean isAssigned() {
        return isAssigned;
    }

    /**
     * Unassigns a Variable. 
     * Used mainly to revert changes performed inside methods to MainScope variables. 
     */
    public void unassign(){
        isAssigned = false;
    }

    /**
     * @return String with the Variable name.
     */
    public String getName() {
        return name;
    }
}
