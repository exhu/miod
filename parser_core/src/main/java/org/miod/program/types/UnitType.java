/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.CompilationUnit;
import org.miod.program.SymItem;
import org.miod.program.SymbolTable;

/**
 *
 * @author yur
 */
public final class UnitType extends TypeSymbol implements SymbolTable {
    private final CompilationUnit unit;
    public UnitType(CompilationUnit unit) {
        this.unit = unit;
    }
    @Override
    public boolean isSymbolTable() {
        return true;
    }

    @Override
    public SymItem get(String id) {
        return unit.symTable.get(id);
    }

    @Override
    public void put(SymItem item) {
        unit.symTable.put(item);
    }

    @Override
    public SymItem resolve(String id) {
        return unit.symTable.resolve(id);
    }
    
    
}
