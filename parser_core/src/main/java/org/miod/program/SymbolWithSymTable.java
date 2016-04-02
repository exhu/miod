/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/**
 *
 * @author yur
 */
public abstract class SymbolWithSymTable extends TypeSymbol implements SymbolTable {
    private final BaseSymbolTable symTable;
    
    SymbolWithSymTable(SymbolTable parent) {
        symTable = new BaseSymbolTable(parent);
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
