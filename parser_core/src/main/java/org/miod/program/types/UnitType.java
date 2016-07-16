/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.CompilationUnit;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolTableItem;

/**
 *
 * @author yur
 */
public final class UnitType extends MiodType implements SymbolTable {
    private final CompilationUnit unit;
    public UnitType(CompilationUnit unit) {
        super(ValueTypeId.UNIT_DEF);
        this.unit = unit;
    }

    @Override
    public SymbolTableItem get(String id) {
        return unit.symTable.get(id);
    }

    @Override
    public void put(SymbolTableItem item) {
        unit.symTable.put(item);
    }

    @Override
    public SymbolTableItem resolve(String id) {
        return unit.symTable.resolve(id);
    }
    
    
}
