package oop.ex6.text_analysis;

import java.io.IOException;

/**
 * Represents a reader used for reading over the body inside methods.
 * 
 * Created by orrp and guybrush on 6/21/15.
 */
public class BodyReader implements MyReader {

    private final String[] body;
    private int curIdx;

    /**
     * creates a body reader over a body provided.
     * @param body a string array with sentences to parse over.
     */
    BodyReader(String[] body){
        this.body = body;
        curIdx = -1;
    }

    /**
     * returns a raw line from the body it's reading.
     * @return a string representing a Sjava sentence.
     * @throws IOException in case of trying to read aline before finishing previous.
     */
    public String getRawLine() throws IOException {
        curIdx++;
        if (curIdx >= body.length){
            return null;
        }
        return body[curIdx];
    }
}
