package oop.ex6.text_analysis;

import oop.ex6.SJavaException;

/**
 * Created by orrp and guybrush on 6/20/15.
 */
public class BadControlFlowStatementException extends SJavaException {

    public String getMessage() {
        return "Error encountered in If/While statement";
    }
}
