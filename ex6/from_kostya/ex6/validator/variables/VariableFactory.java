package oop.ex6.logic_analysis.variables;

/**
 * A singelton Variable Factory with which other modules interact.
 * 
 * Created by orrp and guybrush on 6/15/15.
 */
public class VariableFactory {


    private static VariableFactory instance;

    private VariableFactory(){}

    /**
     * @return the instance of the factory.
     */
    public static VariableFactory getInstance() {
        if (instance == null){
            instance = new VariableFactory();
        }
        return instance;
    }

    /** 
     * the variable constructor.
     * @param isFinal whether the Variable is final or not.
     * @param VarType the type of the Variable according to the VarType enum.
     * @param name the Variable name.
     * @param value whether the Variable is assigned in declaration.
     * @return an instanciated Variable.
     */
    public Variable generateVariable(boolean isFinal, VarType type, String name, String value)
                                                            throws VariableException{
        return new Variable(isFinal, type, name, value);
    }

    /**
     * a bit different variable constructor used mainly by methodDeclaration for paramaters. Those
     * Parameters are the same as Variables only without value so we assign it with it's default
     * value. Method calls must be given assigned variables therefore we created those parameters
     * assigned as well.
     * @param isFinal whether the Variable is final or not.
     * @param VarType the type of the Variable according to the VarType enum.
     * @param name the Variable name.
     * @return an instanciated Variable with that type default value. 
     */
    public Variable generateParameter(boolean isFinal, VarType type, String name) throws
            UnassignedFinalException, IllegalValueException {
        return new Variable(isFinal, type, name, type.getDefaultValue());
    }

    /**
     * copies an existing variable. Uses a "deep copy" approach.
     * @param the Variable we wish to copy.
     * @return a Variable similar to the Variable we provided.
     */
    public Variable copyVariable (Variable toCopy){
        return new Variable(toCopy);
    }
}
