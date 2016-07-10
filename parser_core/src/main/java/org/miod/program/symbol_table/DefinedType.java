/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public final class DefinedType extends SymbolType {
    public final SymbolTableItem symbol;
    public DefinedType(SymbolTableItem symbol) {
        this.symbol = symbol;
    }

}
