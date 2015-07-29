package oop.ex6.logic_analysis;

import oop.ex6.logic_analysis.scope.ScopeException;

/**
 * Created by orrp and guybrush on 6/15/15.
 */
public class UndeclaredMethodCallException extends ScopeException {

    public String getMessage() {
        return "Attempted to call non-declared method";
    }
}
