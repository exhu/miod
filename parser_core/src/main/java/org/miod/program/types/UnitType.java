/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.symbol_table.SymbolTableItem;

/**
 *
 * @author yur
 */
public final class UnitType extends MiodType {
    public SymbolTableItem symbol;
    public UnitType(SymbolTableItem symbol) {
        super(ValueTypeId.UNIT_DEF);
        this.symbol = symbol;
    }

    @Override
    public MiodType resolve() {
        return symbol.resolveAlias().desc.type.resolve();
    }

}