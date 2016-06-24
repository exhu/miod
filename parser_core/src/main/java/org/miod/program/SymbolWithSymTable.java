/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import org.miod.parser.ErrorListener;
import org.miod.program.types.TypeSymbol;

/**
 *
 * @author yur
 */
public abstract class SymbolWithSymTable extends TypeSymbol implements SymbolTable {
    private final BaseSymbolTable symTable;
    
    public SymbolWithSymTable(SymbolTable parent, ErrorListener errorListener) {
        symTable = new BaseSymbolTable(parent, errorListener);
    }
    
    @Override
    final public boolean isSymbolTable() {
        return true;
    }
    
    @Override
    public SymItem resolve(String id) {
        return symTable.resolve(id);
    }
    
    @Override
    public void put(SymItem item) {
        symTable.put(item);
    }
    
    @Override
    public SymItem get(String id) {
        return symTable.get(id);
    }
}
