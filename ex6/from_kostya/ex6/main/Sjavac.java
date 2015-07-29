package oop.ex6.main;

import oop.ex6.SJavaException;
import oop.ex6.text_analysis.Parser;

import java.io.File;
import java.io.IOException;

/**
 * The main program the starts and runs the Sjava verifier.
 * 
 * Created by orrp and guybrush on 6/21/15.
 */
public class Sjavac {


    private static final String SUCCESS = "0";
    private static final String SJAVA_ERROR = "1";
    private static final String IO_ERROR = "2";
    private static final String IO_ERROR_MSG = "IO error occurred.";
    private static final String ARGS_ERROR_MSG = "Usage: java pp[.ex6.main.Sjavac source.file.name";

    /**
     * Main that runs over a file to checks.
     * @param args the file name.
     */
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println(IO_ERROR+"\n"+ARGS_ERROR_MSG);
            return;
        }
        File sourceFile = new File(args[0]);
        try{
            Parser parser = Parser.init(sourceFile);
            parser.parse();
        }catch (IOException ioe){
            System.out.println(IO_ERROR+"\n"+IO_ERROR_MSG);
//            ioe.printStackTrace(); //todo this is for debugging
            return;
        } catch (SJavaException e) {
            System.out.println(SJAVA_ERROR+"\n"+e.getMessage());
//            e.printStackTrace(); //todo this is for debug
            return;
        }
        System.out.println(SUCCESS);
    }
}
