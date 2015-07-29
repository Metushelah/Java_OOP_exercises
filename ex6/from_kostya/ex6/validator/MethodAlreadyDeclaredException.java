package oop.ex6.logic_analysis;

import oop.ex6.logic_analysis.scope.ScopeException;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class MethodAlreadyDeclaredException extends ScopeException {

    public String getMessage() {
        return "Method of the same name already exists";
    }
}
