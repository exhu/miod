/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testc1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.*;

/**
 *
 * @author yuryb
 */
public class Testc1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ANTLRInputStream input = new ANTLRInputStream(System.in);
            testc1gLexer lexer = new testc1gLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            testc1gParser parser = new testc1gParser(tokens);
            parser.program();
        } catch (RecognitionException ex) {
            Logger.getLogger(Testc1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Testc1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
