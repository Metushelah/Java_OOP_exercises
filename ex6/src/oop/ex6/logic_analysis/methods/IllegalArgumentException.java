package oop.ex6.logic_analysis.methods;

import oop.ex6.logic_analysis.scope.ScopeException;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class IllegalArgumentException extends ScopeException {

    public String getMessage() {
        return "Bad argument provided at method call";
    }
}
