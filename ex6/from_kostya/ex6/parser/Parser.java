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
import java.util.regex.Matcher;
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
    private static final String RETURN = "return";
    private static final String FINAL = "final";
    private static final String EQUALS = "=";
    private static final String DECLARATION_DELIMITER = ",";
    private static final String VOID = "void";
    private static final String L_PARENTHESES = "(";
    private static final String R_PARENTHESES = ")";
    private static final String L_CURLY_BRACKET = "{";
    private static final String R_CURLY_BRACKET = "}";
    private static final Pattern L_CURLY_SUFFIX = Pattern.compile("\\{\\s*$");
    private static final Pattern R_CURLY_LINE = Pattern.compile("^\\s*}\\s*$");
    private static final String IF = "if";
    private static final String WHILE = "while";
    private static final String SEMICOLON = ";";
    // stands for all tokens that can come not seperated by themselves.
    private static final Pattern NON_SEPARATED_TOKENS = Pattern.compile("(?<!\\s)?([\\"+
            L_PARENTHESES+EQUALS+DECLARATION_DELIMITER+"\\"+R_PARENTHESES+"\\"+L_CURLY_BRACKET+
            R_CURLY_BRACKET+SEMICOLON+"])(?!\\s)?");
    // lines the can mainly be ignored in the files
    private static final Pattern IGNORED_LINE = Pattern.compile("^(?:\\s*|\\/{2}.*)");
    // what types of conditionals can appear inside if/while conditions.
    private static final VarType[] CONDITION_TYPES_ARRAY =
                                                    {VarType.BOOLEAN, VarType.INT, VarType.DOUBLE};
    private static final ArrayList<VarType> CONDITION_TYPES = // So that we may use contains()
                                            new ArrayList<>(Arrays.asList(CONDITION_TYPES_ARRAY));
    private static final String OR = "||";
    private static final String AND = "&&";
    private static final Pattern WHITESPACES = Pattern.compile("\\s+");

    /* We though of doing a sanity check against characters from other languages but it didn't
     * implement very well so far and all our other methods check for their right syntax so it's 
     * not used for now.
     * 
     * private static final String SUPPORTED_CHARS = "a-zA-Z\\d;_\\{\\},=\\(\\)\\s-\\.";
     * private static final Pattern UNSUPPORTED_CHARS =
     *                                           Pattern.compile("[^"+SUPPORTED_CHARS+"]|.+\\/{2}");
     */

    /*
     * the pareser is a singelton but created a new for each file.
     */
    private static Parser instance;

    private int literals;

    private Scope scope;

    private MyReader reader;


    private String[] curSentence;
    private int curTokenIdx;


    private Parser(File file) throws IOException {
        reader = new MainReader(file);
        literals = 0;
    }

    /**
     * Initializes the text_analysis.
     * @param the file to parse.
     * @return a text_analysis.
     */
    public static Parser init(File file) throws IOException {
        instance = new Parser(file);
        instance.scope = MainScope.init();
        return instance;
    }

    /**
     * @return the parser singelton instance.
     */
    public static Parser getInstance() throws IOException {
        //Assumes not null because of init()
        return instance;
    }

    /*
     * the method reads the next token and evaluates if it's a legal use for Variable name.
     */
    private String varName() throws SJavaException {
        String value = nextToken();
        if (!VAR_NAME.matcher(value).matches() || RESERVED_WORDS.matcher(value).matches()) {
            return null;
        }
        return value;
    }

    /*
     * the method reads the next token and evaluates if it's a legal use for method name.
     */
    private String methodName() throws SJavaException {
        String value = nextToken();
        if (!METHOD_NAME.matcher(value).matches() || RESERVED_WORDS.matcher(value).matches()) {
            return null;
        }
        return value;
    }

    /*
     * evaluates if the next token is a @RETURN token.
     */
    private boolean returns() throws SJavaException {
        if (nextToken().equals(RETURN)){
            if (!nextToken().equals(SEMICOLON)){
                throw new UnexpectedEOLExcpetion();
            }
            return true;
        }
        resetTokenIdx();
        return false;
    }

    /*
     * This is a "sentence" method that tries to parse the line as a Variable declaration.
     * return False if the first 2 tokens are not variable declaration, otherwise assumes you
     * tried to declare a variable and throws exceptions if the syntax failed. 
     * returns True if all went well.
     */
    private boolean declareVariables() throws SJavaException {
        boolean isFinal = (nextToken().equals(FINAL));
        VarType type;
        if (isFinal){
            type = VarType.stringToType(nextToken());
        }
        else{
            type = VarType.stringToType(peekToken());
        }
        if (type == null){
            if (isFinal) {
                throw new BadVariableDeclarationException();
            }
            resetTokenIdx();
            return false; // We had no variable declaration
        }
        do{
            String name = varName();
            if (name == null){
                throw new BadVariableDeclarationException();
            }
            String value = null;
            if (nextToken().equals(EQUALS)){
                value = varName();
                if (value != null){
                    Variable variable = scope.findVariable(value);
                    if (variable == null || !variable.isAssigned() || !type.isA(variable.getType())){
                        throw new BadVariableDeclarationException(); // Tried to assign to a bad var
                    }
                    value = type.getDefaultValue();
                }
                else{
                    value = peekToken();
                }
                nextToken();
            }
            Variable variable = VariableFactory.getInstance().generateVariable(isFinal,
                                                                                type, name, value);
            scope.declareVariable(variable);
        }while(peekToken().equals(DECLARATION_DELIMITER));
        if (!peekToken().equals(SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /*
     * This is a "sentence" method trying to assess if the line provided is a Variable assignment.
     * return False if no Variables by that name found otherwise assumes you tried to assign.
     * If there was a Sjava syntax error will throw an exception otherwise returns true.
     * As of now a variable assignment can only occur as a single instance in a line and so it is
     * implemented but it can be easily modified to accompony more assignments.
     */
    private boolean variableAssignment() throws SJavaException {
        String name = varName();
        if (name == null || !nextToken().equals(EQUALS)) {
            resetTokenIdx();
            return false;
        }
        String value = nextToken();
        scope.assignVariable(name, value);
        if (!nextToken().equals(SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /*
     * This is a "sentence" method trying to assess the line as a method declaration (in mainScope).
     * if no void were found will return False as no method declaration started otherwise assumes
     * you tried to declared and will throw exceptions if errors were found.
     * returns True if completed successfuly.
     */
    private boolean declareMethod() throws SJavaException, IOException {
        if (!nextToken().equals(VOID)){
            resetTokenIdx();
            return false;
        }
        String methodName = methodName();
        if (methodName == null){
            throw new BadMethodDeclarationException();
        }
        if (!nextToken().equals(L_PARENTHESES)){
            throw new BadMethodDeclarationException();
        }
        Variable[] parameters = declareParameters();
        if (!peekToken().equals(R_PARENTHESES)){ // because declareParameters ends with nextToken()
            throw new BadMethodDeclarationException();
        }
        if (!nextToken().equals(L_CURLY_BRACKET)){
            throw new BadMethodDeclarationException();
        }

        // We want to make sure the L_CURLY was the last character in the line
        if (curTokenIdx != curSentence.length-1){
            throw new UnexpectedEOLExcpetion();
        }

        String[] body = findMethodBody();
        Method method = MethodFactory.getInstance().generateMethod(methodName, parameters, body);
        MainScope.getInstance().declareMethod(method);
        return true;
    }

    /*
     * a helper function to the methodDeclaration that finds and cuts and saves the body of the
     * method declared to be parsed later.
     * returns a string array of lines.
     */
    private String[] findMethodBody() throws BadMethodDeclarationException, IOException {
        List<String> lines = new LinkedList<>();
        int curlyCounter = 1;
        String line;
        do {
            line = reader.getRawLine();
            if (line == null) {
                throw new BadMethodDeclarationException(); // Body did not close properly
            }
            if (L_CURLY_SUFFIX.matcher(line).find()) {
                curlyCounter++;
            } else if (R_CURLY_LINE.matcher(line).find()) {
                curlyCounter--;
            }
            lines.add(line);
        } while (curlyCounter != 0);
        return lines.toArray(new String[lines.size()]);
    }

    /*
     * a helper function to the declareMethod function that finds and retains all paramaters
     * specified during the method declaration (works differently that Variable declare as it can
     * be final but does not recieve a value to assign).
     * returns a Variable array containing the parameters declared.
     */
    private Variable[] declareParameters() throws SJavaException{
        List<Variable> parameters = new LinkedList<>();
        do{
            boolean isFinal = (nextToken().equals(FINAL));
            VarType type;
            if (isFinal){
                type = VarType.stringToType(nextToken());
            }
            else{
                type = VarType.stringToType(peekToken());
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

            for (Variable existing : parameters){ //todo move this into a static(?) method of its own
                if (existing.getName().equals(parameter.getName())){
                    throw new BadMethodDeclarationException();
                }
            }
            parameters.add(parameter);
        }while(nextToken().equals(DECLARATION_DELIMITER));
        return parameters.toArray(new Variable[parameters.size()]);
    }

    /*
     * This is a "sentence" method trying to assess if the line is a control flow statment (if/while).
     * returns False if no controlFlow were found otherwise assumes you tried to create a control
     * flow and throws exception if errors occured in the syntax.
     * return True if passed successfully.
     */
    private boolean controlFlowStatement() throws SJavaException{
        if (!nextToken().equals(IF) && !peekToken().equals(WHILE)){
            resetTokenIdx();
            return false;
        }
        if (!nextToken().equals(L_PARENTHESES)){
            throw new BadControlFlowStatementException();
        }
        conditionals(); // try to see if conditionals were provided
        // peeks Because conditionals finishes with nextToken().
        if (!peekToken().equals(R_PARENTHESES)){
            throw new BadControlFlowStatementException();
        }
        if (!nextToken().equals(L_CURLY_BRACKET)){
            throw new BadControlFlowStatementException();
        }
        scope = scope.declareSubScope();
        return true;
    }

    /*
     * a helper function to the controlFlowStatment function that checks if conditionals inside
     * the control flow were provided correctly. 
     * Atleast one condition needs to be specified as per Sjava specifications.
     */
    private void conditionals() throws SJavaException{
        do{
            if (VarType.BOOLEAN.getPattern().matcher(nextToken()).matches()){
                continue;
            }
            //Else assume it's a varName
            Variable variable = scope.findVariable(peekToken());
            if (variable == null || !variable.isAssigned() ||
                                        !CONDITION_TYPES.contains(variable.getType())){
                throw new BadControlFlowStatementException();
            }
        }while (nextToken().equals(AND) || peekToken().equals(OR));

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
            resetTokenIdx();
            return false;
        }
        // you can still exit here as maybe you have a method and a Variable with the same name.
        if (!nextToken().equals(L_PARENTHESES)){
            resetTokenIdx();
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
                argument = literal(peekToken());
            }
            if (argument == null){ //no arguments were given.
                break;
            }
            arguments.add(argument);
        }while(nextToken().equals(DECLARATION_DELIMITER));
        if (!peekToken().equals(R_PARENTHESES)){
            throw new BadMethodCallException();
        }
        // compare to existing declared methods.
        MainScope.getInstance().callMethod(methodName, arguments);
        if (!nextToken().equals(SEMICOLON)){
            throw new UnexpectedEOLExcpetion();
        }
        return true;
    }

    /* 
     * This method helps check on  method calls when provided with simply a value instead of existing
     * Variables. This relies on the fact that var names cannot be "i", where i is an integer so we
     * use this to save them as variables and work with the regular API we have. 
     * For more info, see README.
     * recieves a string with a value and turns it into a variable for us to use with correct type.
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
        if (nextToken().equals(R_CURLY_BRACKET)){
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
            getNextSentence();
            if (curSentence != null && closeBody()){
                return false;
            }
            return true;
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
     */
    //todo do we need to add public @throws when exceptions are thrown?
    public void parse() throws IOException, SJavaException {
        /* Parse the main scope */
        getNextSentence();
        while (curSentence != null) {
            mainSentence();
            getNextSentence();
        }
        MainScope.getInstance().updateUnassignedGlobals();

        /* Main scope parsed, now dive deeper into the methods*/
        Collection<Method> methods = MainScope.getInstance().getMethods();

        for (Method method : methods){
            reader = new BodyReader(method.getBody());
            scope = MainScope.getInstance().declareMethodBody(method);
            do{
                getNextSentence();
                if (curSentence == null){
                    throw new UnexpectedEndOfBlockException();
                }
            }while (methodSentence());
            MainScope.getInstance().revertGlobals();
        }
    }


    /* 
     * This function returns the next "token" in our line as String.
     * Assumes there is an additional token following. Throws exception otherwise. 
     */
    private String nextToken() throws SJavaException{
        curTokenIdx++;
        if (curTokenIdx >= curSentence.length){
            throw new UnexpectedEOLExcpetion();
        }
        return curSentence[curTokenIdx];
    }

    /* 
     * Receives a raw line and it manipulates it to only retain it's "tokens".
     * Operates by adding spaces to any non-whitespace-separated tokens, and then splits by
     * whitespace.
     */
    private static String[] splitRawLine(String rawLine){
        Matcher matcher = NON_SEPARATED_TOKENS.matcher(rawLine);
        String sentence = matcher.replaceAll(" $1 ");
        sentence = WHITESPACES.matcher(sentence).replaceAll(" ");
        return sentence.trim().split(" ");
    }

    /* 
     * returns the next sentence to parse. This method should only be called when the previous
     * sentence was parsed in its entirety. 
     */
    private void getNextSentence() throws SJavaException, IOException {
        if (curSentence != null && curTokenIdx != curSentence.length-1){ // null check for first time
            throw new UnexpectedEOLExcpetion();
        }
        resetTokenIdx();

        String sentence = reader.getRawLine();
        while (sentence != null && IGNORED_LINE.matcher(sentence).matches()){
            sentence = reader.getRawLine();
        }
        if (sentence == null){
            curSentence = null;
            return;
        }
       /* Sanity check - only supported characters appear, not implemented right now.
		*	if (UNSUPPORTED_CHARS.matcher(sentence).find()){
  		*		throw new UnexpectedCharacterException();
		*	}
		*/
        curSentence = splitRawLine(sentence);
    }
    
    /*
     * resets the token to the lines start. Used mainly when failing to assess a line as a certain
     * type of sentence, so the next can work properly. 
     */
    private void resetTokenIdx(){
        curTokenIdx = -1;
    }
    
    /*
     * returns a string containing the next token in the line.
     */
    private String peekToken(){
        return curSentence[curTokenIdx];
    }
}
