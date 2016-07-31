/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTableItem;

/**
 *
 * @author yur
 */
public final class VarSymbol extends SymbolTableItem {
    public VarSymbol(SymbolDesc desc) {
        super(desc);
    }
}
