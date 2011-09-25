/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testc1;

/**
 *
 * @author yuryb
 */
public class Symbol {
    String name;
    Type type;
    
    
    
    public Symbol(String n, Type t) {
        name = n;
        type = t;
    }
    
    public Symbol(String n) {
        name = n;
    }
    
    public String getName() {
        return name;
    }
}
