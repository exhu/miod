/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testc1;

/**
 *
 * @author yuryb
 */
public interface Scope {
    String getScopeName();
    Scope getEnclosingScope();
    void defineSymbol(Symbol s);
    Symbol resolveSymbol(String name);
}
