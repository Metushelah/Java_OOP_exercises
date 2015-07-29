package oop.ex6.text_analysis;

import oop.ex6.SJavaException;
import oop.ex6.logic_analysis.scope.MainScope;
import oop.ex6.logic_analysis.scope.Scope;
import oop.ex6.logic_analysis.methods.Method;
import oop.ex6.logic_analysis.methods.MethodFactory;
import oop.ex6.logic_analysis.variables.VarType;
import oop.ex6.logic_analysis.variables.Variable;
import oop.ex6.logic_analysis.variables.VariableFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The parser that goes over the Sjava file and uses the other modules to check for correctness.
 * 
 * Created by orrp and guybrush on 6/16/15.
 */
public class Parser {


	/*
	 * Constants according to the Sjava syntax specification.
	 */
    private static final Pattern RESERVED_WORDS = Pattern.compile("(?:int|double|boolean|" +
                                            "char|String|void|final|if|while|true|false|return)");
    private static final Pattern VAR_NAME =Pattern.compile("^(?:_[_a-zA-Z]|[a-zA-Z])[_a-zA-Z\\d]*$");
    private static final Pattern METHOD_NAME = Pattern.compile("^[a-zA-Z][_a-zA-Z\\d]*$");
    // what types of conditionals can appear inside if/while conditions.
    private static final VarType[] CONDITION_TYPES_ARRAY =
                                                    {VarType.BOOLEAN, VarType.INT, VarType.DOUBLE};
    private static final ArrayList<VarType> CONDITION_TYPES = // So that we may use contains()
                                            new ArrayList<>(Arrays.asList(CONDITION_TYPES_ARRAY));

    /* We though of doing a sanity check against characters from other languages but it didn't
     * implement very well so far and all our other methods check for their right syntax so it's 
     * not used for now.
     * 
     * private static final String SUPPORTED_CHARS = "a-zA-Z\\d;_\\{\\},=\\(\\)\\s-\\.";
     * private static final Pattern UNSUPPORTED_CHARS =
     *                                           Pattern.compile("[^"+SUPPORTED_CHARS+"]|.+\\/{2}");
     */

    /*
     * the parser is a singleton but created a new for each file.
     */
    private static Parser instance;

    private int literals;

    private Scope scope;

    private Lexer lexer;


    private Parser() throws IOException {
        literals = 0;
    }

    /**
     * Initializes the text_analysis.
     * @param file the file to parse.
     * @return a text_analysis.
     * @throws IOException when could not initialize file.
     */
    public static Parser init(File file) throws IOException {
        instance = new Parser();
        instance.lexer = Lexer.init(file);
        instance.scope = MainScope.init();
        return instance;
    }

    /*
     * the method reads the next token and evaluates if it's a legal use for Variable name.
     */
    private String varName() throws SJavaException {
        String value = lexer.nextToken();
        if (!VAR_NAME.matcher(value).matches() || RESERVED_WORDS.matcher(value).matches()) {
            return null;
        }
        return value;
    }

    /*
     * the method reads the next token and evaluates if it's a legal use for method name.
     */
    private String methodName() throws SJavaException {
        String value = lexer.nextToken();
        if (!METHOD_NAME.matcher(value).matches() || RESERVED_WORDS.matcher(value).matches()) {
            return null;
        }
        return value;
    }

    /*
     * evaluates if the next token is a @RETURN token.
     */
    private boolean returns() throws SJavaException {
        if (lexer.nextToken().equals(Symbols.RETURN)){
            if (!lexer.nextToken().equals(Symbols.SEMICOLON)){
                throw new UnexpectedEOLExcpetion();
            }
            return true;
        }
        lexer.resetTokenIdx();
        return false;
    }

    /*
     * This is a "sentence" method that tries to parse the line as a Variable declaration.
     * return False if the first 2 tokens are not variable declaration, otherwise assumes you
     * tried to declare a variable and throws exceptions if the syntax failed.
     * returns True if all went well.
     */
    private boolean declareVariables() throws SJavaException {
        boolean isFinal = (lexer.nextToken().equals(Symbols.FINAL));
        VarType type;
        if (isFinal){
            type = VarType.stringToType(lexer.nextToken());
        }
        else{
            type = VarType.stringToType(lexer.peekToken());
        }
        if (type == null){
            if (isFinal) {
                throw new BadVariableDeclarationException();
            }
            lexer.resetTokenIdx();
            return false; // We had no variable declaration
        }
        do{
            String name = varName();
            if (name == null){
                throw new BadVariableDeclarationException();
            }
            String value = null;
            if (lexer.nextToken().equals(Symbols.EQUALS)){
                value = varName();
                if (value != null){
                    Variable variable = scope.findVariable(value);
                    if (variable == null || !variable.isAssigned() || !type.isA(variable.getType())){
                        throw new BadVariableDeclarationException(); // Tried to assign to a bad var
                    }
                    value = type.getDefaultValue();
                }
                else{
                    value = lexer.peekToken();
                }
                lexer.nextToken();
            }
            Variable variable = VariableFactory.getInstance().generateVariable(isFinal,
                                                                                type, name, value);
            scope.declareVariable(variable);
        }while(lexer.peekToken().equals(Symbols.DECLARATION_DELIMITER));
        if (!lexer.peekToken().equals(Symbols.SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /*
     * This is a "sentence" method trying to assess if the line provided is a Variable assignment.
     * return False if no Variables by that name found otherwise assumes you tried to assign.
     * If there was a Sjava syntax error will throw an exception otherwise returns true.
     * As of now a variable assignment can only occur as a single instance in a line and so it is
     * implemented but it can be easily modified to accompany more assignments.
     */
    private boolean variableAssignment() throws SJavaException {
        String name = varName();
        if (name == null || !lexer.nextToken().equals(Symbols.EQUALS)) {
            lexer.resetTokenIdx();
            return false;
        }
        String value = lexer.nextToken();
        scope.assignVariable(name, value);
        if (!lexer.nextToken().equals(Symbols.SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /*
     * This is a "sentence" method trying to assess the line as a method declaration (in mainScope).
     * if no void were found will return False as no method declaration started otherwise assumes
     * you tried to declared and will throw exceptions if errors were found.
     * returns True if completed successfully.
     */
    private boolean declareMethod() throws SJavaException, IOException {
        if (!lexer.nextToken().equals(Symbols.VOID)){
            lexer.resetTokenIdx();
            return false;
        }
        String methodName = methodName();
        if (methodName == null){
            throw new BadMethodDeclarationException();
        }
        if (!lexer.nextToken().equals(Symbols.L_PARENTHESES)){
            throw new BadMethodDeclarationException();
        }
        Variable[] parameters = declareParameters();
        if (!lexer.peekToken().equals(Symbols.R_PARENTHESES)){
            // because declareParameters ends with lexer.nextToken()
            throw new BadMethodDeclarationException();
        }
        if (!lexer.nextToken().equals(Symbols.L_CURLY_BRACKET)){
            throw new BadMethodDeclarationException();
        }

        // We want to make sure the L_CURLY was the last character in the line
        if (!lexer.isEndOfSentence()){
            throw new UnexpectedEOLExcpetion();
        }

        String[] body = lexer.findMethodBody();
        Method method = MethodFactory.getInstance().generateMethod(methodName, parameters, body);
        MainScope.getInstance().declareMethod(method);
        return true;
    }

    /*
     * a helper function to the declareMethod function that finds and retains all parameters
     * specified during the method declaration (works differently that Variable declare as it can
     * be final but does not receive a value to assign).
     * returns a Variable array containing the parameters declared.
     */
    private Variable[] declareParameters() throws SJavaException{
        List<Variable> parameters = new LinkedList<>();
        do{
            boolean isFinal = (lexer.nextToken().equals(Symbols.FINAL));
            VarType type;
            if (isFinal){
                type = VarType.stringToType(lexer.nextToken());
            }
            else{
                type = VarType.stringToType(lexer.peekToken());
            }
            if (type == null){
                if (isFinal) {
                    throw new BadMethodDeclarationException();
                }
                return null;
            }
            String name = varName();
            if (name == null){
                throw new BadMethodDeclarationException();
            }
            Variable parameter = VariableFactory.getInstance().generateParameter(isFinal, type, name);

            for (Variable existing : parameters){
                if (existing.getName().equals(parameter.getName())){
                    throw new BadMethodDeclarationException();
                }
            }
            parameters.add(parameter);
        }while(lexer.nextToken().equals(Symbols.DECLARATION_DELIMITER));
        return parameters.toArray(new Variable[parameters.size()]);
    }

    /*
     * This is a "sentence" method trying to assess if the line is a control flow statement (if/while).
     * returns False if no controlFlow were found otherwise assumes you tried to create a control
     * flow and throws exception if errors occurred in the syntax.
     * return True if passed successfully.
     */
    private boolean controlFlowStatement() throws SJavaException{
        if (!lexer.nextToken().equals(Symbols.IF) && !lexer.peekToken().equals(Symbols.WHILE)){
            lexer.resetTokenIdx();
            return false;
        }
        if (!lexer.nextToken().equals(Symbols.L_PARENTHESES)){
            throw new BadControlFlowStatementException();
        }
        conditionals(); // try to see if conditionals were provided
        // peeks Because conditionals finishes with lexer.nextToken().
        if (!lexer.peekToken().equals(Symbols.R_PARENTHESES)){
            throw new BadControlFlowStatementException();
        }
        if (!lexer.nextToken().equals(Symbols.L_CURLY_BRACKET)){
            throw new BadControlFlowStatementException();
        }
        scope = scope.declareSubScope();
        return true;
    }

    /*
     * a helper function to the controlFlowStatement function that checks if conditionals inside
     * the control flow were provided correctly.
     * At least one condition needs to be specified as per Sjava specifications.
     */
    private void conditionals() throws SJavaException{
        do{
            if (VarType.BOOLEAN.getPattern().matcher(lexer.nextToken()).matches()){
                continue;
            }
            //Else assume it's a varName
            Variable variable = scope.findVariable(lexer.peekToken());
            if (variable == null || !variable.isAssigned() ||
                                        !CONDITION_TYPES.contains(variable.getType())){
                throw new BadControlFlowStatementException();
            }
        }while (lexer.nextToken().equals(Symbols.AND) || lexer.peekToken().equals(Symbols.OR));

    }

    /*
     * This is a "sentence" method trying to assess the line as a method calling.
     * returns False if no existing method name found as it might not having been a call otherwise
     * assumes you did try and thus continues and throws exceptions if errors in syntax were found.
     * returns True if callMethod completed successfully.
     */
    private boolean callMethod() throws SJavaException {
        String methodName = methodName();
        if (methodName == null){
            lexer.resetTokenIdx();
            return false;
        }
        // you can still exit here as maybe you have a method and a Variable with the same name.
        if (!lexer.nextToken().equals(Symbols.L_PARENTHESES)){
            lexer.resetTokenIdx();
            return false;
        }

        // reads the arguments provided if any.
        Queue<Variable> arguments = new LinkedList<>();
        do{
            Variable argument;
            String varName = varName();
            if (varName != null) {
                argument = scope.findVariable(varName);
            }
            else { // It may only be a literal (or invalid)
                argument = literal(lexer.peekToken());
            }
            if (argument == null){ //no arguments were given.
                break;
            }
            arguments.add(argument);
        }while(lexer.nextToken().equals(Symbols.DECLARATION_DELIMITER));
        if (!lexer.peekToken().equals(Symbols.R_PARENTHESES)){
            throw new BadMethodCallException();
        }
        // compare to existing declared methods.
        MainScope.getInstance().callMethod(methodName, arguments);
        if (!lexer.nextToken().equals(Symbols.SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /*
     * This method helps check on  method calls when provided with simply a value instead of existing
     * Variables. This relies on the fact that var names cannot be "i", where i is an integer so we
     * use this to save them as variables and work with the regular API we have.
     * For more info, see README.
     * receives a string with a value and turns it into a variable for us to use with correct type.
     * returns a Variable.
     */
    private Variable literal(String value) throws SJavaException {
        String name = String.valueOf(literals);
        VarType type = VarType.literal(value);
        if (type == null){
            return null;
        }
        literals++;
        return VariableFactory.getInstance().generateVariable(false, type, name, value);
    }

    /*
     * this method attempts to close an inner scope which arrives condition is met.
     * returns True or False if scoped closed or not.
     */
    private boolean closeBody() throws SJavaException{
        if (lexer.nextToken().equals(Symbols.R_CURLY_BRACKET)){
            scope = scope.getParent();
            if (scope == null){
                throw new UnrecognizedSyntaxException();
            }
            return true;
        }
        return false;
    }

    /*
     * This is the method trying to assess sentences as a MainScope kind of sentences.
     * If no recognized sentence were received will throw an exception.
     */
    private void mainSentence() throws SJavaException, IOException {
        if (!declareMethod() && !declareVariables() && !variableAssignment()){
            throw new UnrecognizedSyntaxException();
        }
    }

    /*
     * This method is trying to assess a sentence as a sentence inside a method. It is on the watch
     * for the conditions that closes the method body and returns True or False whether we are
     * StillInBody. Inside method more scopes can be opened so it delegates to secondarySentence if
     * a scope if greater then 1. For more information refer to README.
     */
    private boolean methodSentence() throws SJavaException, IOException {
        if (scope.getParent() != MainScope.getInstance()){ // So we are in a secondary scope,
            secondarySentence();                        // not the methods's main scope.
            return true;
        }

        // check for method body closing condition.
        if (returns()){
            lexer.getNextSentence();
            return !(!lexer.isEndOfStream() && closeBody());
        }
        // closing condition wasn't met than we continue as normal.
        if (!controlFlowStatement() && !declareVariables() && !callMethod()
                                                                && !variableAssignment()){
            throw new UnrecognizedSyntaxException();
        }
        return true;
    }

    /*
     * This is a method trying to assess a line as sentence in scopes bigger than 1.
     * For more information refer to README.
     */
    private void secondarySentence() throws SJavaException{
        if (!controlFlowStatement() && !declareVariables() && !callMethod() && !variableAssignment()
                                        && !returns() && !closeBody()){
            throw new UnrecognizedSyntaxException();
        }
    }

    /**
     * The main function that parses the file. Will run over the file and try to see if Sjava syntax
     * is kept and maintained.
     * @throws IOException when failed to read line properly.
     * @throws SJavaException catches the other exceptions created not according to Sjava rules.
     */
    public void parse() throws IOException, SJavaException {
        /* Parse the main scope */
        lexer.getNextSentence();
        while (!lexer.isEndOfStream()) {
            mainSentence();
            lexer.getNextSentence();
        }
        MainScope.getInstance().updateUnassignedGlobals();

        /* Main scope parsed, now dive deeper into the methods*/
        Collection<Method> methods = MainScope.getInstance().getMethods();

        for (Method method : methods){
            lexer.readFromBody(method.getBody());
            scope = MainScope.getInstance().declareMethodBody(method);
            do{
                lexer.getNextSentence();
                if (lexer.isEndOfSentence()){
                    throw new UnexpectedEndOfBlockException();
                }
            }while (methodSentence());
            MainScope.getInstance().revertGlobals();
        }
    }
    
    
}
