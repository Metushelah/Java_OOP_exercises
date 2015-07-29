package oop.ex6.text_analysis;

import oop.ex6.SJavaException;

/**
 * Created by orrp and guybrush on 6/20/15.
 */
public class BadLiteralExcepetion extends SJavaException {

    public String getMessage() {
        return "Bad literal value encountered";
    }
}
