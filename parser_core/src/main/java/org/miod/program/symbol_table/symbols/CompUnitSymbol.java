/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.types.UnitType;

/** Used to prevent redefinitions?.
 *
 * @author yur
 */
public final class CompUnitSymbol extends SymbolTableItem {
    public CompUnitSymbol(SymbolLocation location) {
        super(new SymbolDesc(location.unitName, location,
                null, SymbolVisibility.PRIVATE));
        desc.type = new UnitType(this);
    }

    @Override
    public String toString() {
        return String.format("CompUnitSymbol(%s)", desc.name);
    }


}
