package oop.ex6.logic_analysis.variables;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class UnassignedFinalException extends VariableException {

    public String getMessage() {
        return "Final variable must be assigned at declaration";
    }
}
