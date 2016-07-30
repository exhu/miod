/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.program.CompilationUnit;
import org.miod.program.types.UnitType;

/**
 *
 * @author yur
 */
public final class CompUnitSymbol extends SymbolTableItem {
    public CompUnitSymbol(CompilationUnit unit, String name, SymbolLocation location) {
        super(new SymbolDesc(name, name, location, SymbolKind.Unit,
                new UnitType(unit), SymbolVisibility.Private));
    }
}
