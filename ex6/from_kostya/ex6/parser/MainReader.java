package oop.ex6.text_analysis;

import java.io.*;

/**
 * A reader that goes over a file and reads it line by line.
 * 
 * Created by orrp and guybrush on 6/21/15.
 */
public class MainReader implements MyReader {

    private BufferedReader reader;

    /**
     * Reader constructor over a file to read.
     * @param file the file to read.
     */
    MainReader(File file) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
    }

    /**
     * returns a line from the file.
     * @return a string representing representing a sentence.
     */
    public String getRawLine() throws IOException {
        return reader.readLine();
    }
}
