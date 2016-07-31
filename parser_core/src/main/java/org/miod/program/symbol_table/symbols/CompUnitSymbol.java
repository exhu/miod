/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.types.UserType;
import org.miod.program.types.ValueTypeId;

/**
 *
 * @author yur
 */
public final class CompUnitSymbol extends SymbolTableItem {
    public CompUnitSymbol(SymbolLocation location) {
        super(new SymbolDesc(location.unitName, location,
                null, SymbolVisibility.Private));
        desc.type = new UserType(ValueTypeId.UNIT_DEF, this);
    }
}
