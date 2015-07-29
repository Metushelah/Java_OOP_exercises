package oop.ex6.text_analysis;

import java.io.IOException;

/**
 * Interface to implement the way we want our reader to behave, read one line each time.
 * 
 * Created by orrp and guybrush on 6/21/15.
 */
public interface MyReader {

    /**
     * will be implemented in each reader
     * @return a String containing the line
     * @throws IOException in case of errors in reading line
     */
    public String getRawLine() throws IOException;
}
