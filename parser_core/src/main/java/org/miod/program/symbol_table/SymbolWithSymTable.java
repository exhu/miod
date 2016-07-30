/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.parser.ErrorListener;

/**
 *
 * @author yur
 */
public abstract class SymbolWithSymTable extends SymbolTableItem implements SymbolTable {
    private final BasicSymbolTable symTable;
    
    public SymbolWithSymTable(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(desc);
        symTable = new BasicSymbolTable(parent, errorListener);
    }
    
    @Override
    public SymbolTableItem resolve(String id) {
        return symTable.resolve(id);
    }
    
    @Override
    public void put(SymbolTableItem item) {
        symTable.put(item);
    }
    
    @Override
    public SymbolTableItem get(String id) {
        return symTable.get(id);
    }
}
