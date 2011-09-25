/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testc1;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yuryb
 */
public class SymbolTable implements Scope {
    Map<String, Symbol> symbols = new HashMap<String, Symbol>();
    
    public SymbolTable() {
        initSymbols();
    }
    
    private void initSymbols() {
        symbols.put("float", new BuiltInTypeSymbol("float"));
    }

    @Override
    public String getScopeName() {
        return "global";
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public void defineSymbol(Symbol s) {
        symbols.put(s.name, s);
    }

    @Override
    public Symbol resolveSymbol(String name) {
        return symbols.get(name);
    }
    
    @Override
    public String toString() {
        return symbols.toString();
    }
    
}
