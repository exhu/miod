/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolWithSymTable;

/** Populate arguments with SymbolTable method put.
 *
 * @author yur
 */
public final class ProcSymbol extends SymbolWithSymTable {
    public final StatementBlock block;
    public ProcSymbol(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(parent, desc, errorListener);
        block = new StatementBlock(parent, errorListener);
    }
}
