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
public final class IntegerInBoundsExpected extends CompilerError {
    final int low, high;
    public IntegerInBoundsExpected(SymbolLocation location, int low, int high) {
        super(location);
        this.low = low;
        this.high = high;
    }

}
