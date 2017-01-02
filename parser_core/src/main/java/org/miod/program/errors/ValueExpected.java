/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

import org.miod.program.symbol_table.SymbolLocation;

/**
 *
 * @author yur
 */
public final class ValueExpected extends CompilerError {
    public ValueExpected(SymbolLocation loc) {
        super(loc);
    }
}
