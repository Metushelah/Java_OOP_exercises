package oop.ex6.logic_analysis;

import oop.ex6.logic_analysis.scope.ScopeException;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class VariableAlreadyDeclaredException extends ScopeException {

    public String getMessage() {
        return "Variable of the same name already exists";
    }
}
