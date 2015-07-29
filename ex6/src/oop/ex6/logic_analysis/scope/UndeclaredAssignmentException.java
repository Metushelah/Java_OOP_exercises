package oop.ex6.logic_analysis.scope;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
/* To be thrown when attempting to assign an undeclared variable */
public class UndeclaredAssignmentException extends ScopeException{

    public String getMessage() {
        return "Attempted to assign to non-declared variable";
    }
}
