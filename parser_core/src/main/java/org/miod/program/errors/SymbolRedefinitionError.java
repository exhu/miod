/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

import org.miod.program.symbol_table.SymbolTableItem;

/**
 *
 * @author yur
 */
public final class SymbolRedefinitionError extends CompilerError {
    public SymbolRedefinitionError(SymbolTableItem sym) {
        // TODO error message
        super(sym.toString());
    }
}
