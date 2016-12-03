/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public final class ConstSymbol extends SymbolTableItem {
    public final MiodValue value;

    public ConstSymbol(SymbolDesc desc, MiodValue value) {
        super(desc);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("ConstSymbol(%s)", value.toString());
    }


}