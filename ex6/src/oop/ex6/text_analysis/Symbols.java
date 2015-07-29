package oop.ex6.text_analysis;

import java.util.regex.Pattern;

/**
 * Created by orrp and guybrush on 6/23/15.
 */
class Symbols {


    static final String RETURN = "return";
    static final String FINAL = "final";
    static final String EQUALS = "=";
    static final String DECLARATION_DELIMITER = ",";
    static final String VOID = "void";
    static final String L_PARENTHESES = "(";
    static final String R_PARENTHESES = ")";
    static final String L_CURLY_BRACKET = "{";
    static final String R_CURLY_BRACKET = "}";
    static final Pattern L_CURLY_SUFFIX = Pattern.compile("\\{\\s*$");
    static final Pattern R_CURLY_LINE = Pattern.compile("^\\s*}\\s*$");
    static final String IF = "if";
    static final String WHILE = "while";
    static final String SEMICOLON = ";";
    static final String OR = "||";
    static final String AND = "&&";

    private Symbols(){}
}
