package oop.ex6.logic_analysis.variables;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class IllegalValueExcpetion extends VariableException {

    public String getMessage() {
        return "Illegal value provided for variable";
    }
}
