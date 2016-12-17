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
public abstract class CompilerWarning {
    final SymbolLocation location;
    public CompilerWarning(SymbolLocation location) {
        this.location = location;
    }
}
