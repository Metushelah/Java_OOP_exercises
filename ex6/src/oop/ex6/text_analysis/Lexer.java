package oop.ex6.text_analysis;

import oop.ex6.SJavaException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by orrp and guybrush on 6/23/15.
 */
class Lexer {
    // stands for all tokens that can come not separated by themselves.
    private static final Pattern NON_SEPARATED_TOKENS = Pattern.compile("(?<!\\s)?([\\"+
            Symbols.L_PARENTHESES+ Symbols.EQUALS+ Symbols.DECLARATION_DELIMITER+"\\"+
            Symbols.R_PARENTHESES+"\\"+ Symbols.L_CURLY_BRACKET+ Symbols.R_CURLY_BRACKET+
            Symbols.SEMICOLON+"])(?!\\s)?");
    private static final Pattern WHITESPACES = Pattern.compile("\\s+");
    // lines the can mainly be ignored in the files
    private static final Pattern IGNORED_LINE = Pattern.compile("^(?:\\s*|\\/{2}.*)");


    private MyReader reader;

    private String[] curSentence;
    private int curTokenIdx;

    private Lexer(File file) throws FileNotFoundException {
        reader = new MainReader(file);

        curTokenIdx = -1;
    }

    /*
     * Receives a raw line and it manipulates it to only retain it's "tokens".
     * Operates by adding spaces to any non-whitespace-separated tokens, and then splits by
     * whitespace.
     */
    private static String[] lexer(String rawLine){
        Matcher matcher = NON_SEPARATED_TOKENS.matcher(rawLine);
        String sentence = matcher.replaceAll(" $1 ");
        sentence = WHITESPACES.matcher(sentence).replaceAll(" ");
        return sentence.trim().split(" ");
    }

    /*
    * initializes a new lexer for the pre-submit tests.
     */
    static Lexer init(File file) throws FileNotFoundException {
        return new Lexer(file);
    }

    /*
     * returns the next sentence to parse. This method should only be called when the previous
     * sentence was parsed in its entirety.
     */
    void getNextSentence() throws SJavaException, IOException {
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
        curSentence = Lexer.lexer(sentence);
    }

    /*
     * resets the token to the lines start. Used mainly when failing to assess a line as a certain
     * type of sentence, so the next can work properly.
     */
    void resetTokenIdx(){
        curTokenIdx = -1;
    }

    /*
    * This function returns the next "token" in our line as String.
    * Assumes there is an additional token following. Throws exception otherwise.
    */
    String nextToken() throws SJavaException {
        curTokenIdx++;
        if (curTokenIdx >= curSentence.length){
            throw new UnexpectedEOLExcpetion();
        }
        return curSentence[curTokenIdx];
    }

    /*
     * returns a string containing the next token in the line.
     */
    String peekToken(){
        return curSentence[curTokenIdx];
    }

    void readFromBody(String[] body) {
        this.reader = new BodyReader(body);
    }

    boolean isEndOfSentence(){
        return curTokenIdx == curSentence.length-1;
    }

    boolean isEndOfStream(){
        return curSentence == null;
    }

    /*
    * a helper function to the methodDeclaration that finds, cuts and saves the body of the
    * method declared to be parsed later.
    * returns a string array of lines.
    */
    String[] findMethodBody() throws BadMethodDeclarationException, IOException {
        List<String> lines = new LinkedList<>();
        int curlyCounter = 1;
        String line;
        do {
            line = reader.getRawLine();
            if (line == null) {
                throw new BadMethodDeclarationException(); // Body did not close properly
            }
            if (Symbols.L_CURLY_SUFFIX.matcher(line).find()) {
                curlyCounter++;
            } else if (Symbols.R_CURLY_LINE.matcher(line).find()) {
                curlyCounter--;
            }
            lines.add(line);
        } while (curlyCounter != 0);
        return lines.toArray(new String[lines.size()]);
    }
}
