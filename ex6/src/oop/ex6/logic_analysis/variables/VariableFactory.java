package oop.ex6.logic_analysis.variables;

/**
 * A singleton Variable Factory with which other modules interact.
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
     * @param type the type of the Variable according to the VarType enum.
     * @param name the Variable name.
     * @param value whether the Variable is assigned in declaration.
     * @return an instantiated Variable.
     * @throws VariableException when failed to create a variable.
     */
    public Variable generateVariable(boolean isFinal, VarType type, String name, String value)
                                                            throws VariableException{
        return new Variable(isFinal, type, name, value);
    }

    /**
     * a bit different variable constructor used mainly by methodDeclaration for parameters. Those
     * Parameters are the same as Variables only without value so we assign it with it's default
     * value. Method calls must be given assigned variables therefore we created those parameters
     * assigned as well.
     * @param isFinal whether the Variable is final or not.
     * @param type the type of the Variable according to the VarType enum.
     * @param name the Variable name.
     * @return an instantiated Variable with that type default value.
     * @throws UnassignedFinalException passes up the exception created in Variable.
     * @throws IllegalValueException passes up the exception created in Variable.
     */
    public Variable generateParameter(boolean isFinal, VarType type, String name) throws
            UnassignedFinalException, IllegalValueException {
        return new Variable(isFinal, type, name, type.getDefaultValue());
    }
}
