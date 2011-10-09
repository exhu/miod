/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testc1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author yuryb
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("test1src.txt"));// System.in);
            TestC1ScanLexer lexer = new TestC1ScanLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TestC1ScanParser parser = new TestC1ScanParser(tokens);
            
            SymbolTable globals = new SymbolTable();
            
            // TODO 1) use TestC1Scan.g to build AST
            // 2) build tree parser to populate symbol table
            // 3) build tree parser to generate C source
            
            TestC1ScanParser.program_return r = parser.program();
            
            CommonTree t = (CommonTree)r.getTree();// extract AST
            System.out.println(t.toStringTree()); // print out
            
            System.out.println(globals.toString());

        } catch (RecognitionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
