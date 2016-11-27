/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTableItem;

/** SymbolItem entry for enums, structs, classes etc. defined with 'type'
 * keyword.
 *
 * @author yur
 */
public final class TypeDefSymbol extends SymbolTableItem {
    public TypeDefSymbol(SymbolDesc desc) {
        super(desc);
    }
}
