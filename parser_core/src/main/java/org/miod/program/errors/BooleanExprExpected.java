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
public final class BooleanExprExpected extends CompilerError {
    public BooleanExprExpected(SymbolLocation location) {
        super(location);
    }

}
