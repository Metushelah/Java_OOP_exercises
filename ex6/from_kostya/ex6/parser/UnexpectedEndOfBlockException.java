package oop.ex6.text_analysis;

import oop.ex6.SJavaException;

/**
 * Created by orrp and guybrush on 6/21/15.
 */
public class UnexpectedEndOfBlockException extends SJavaException {

    public String getMessage() {
        return "Unexpected end of block encountered";
    }
}
