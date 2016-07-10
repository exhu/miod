/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.program.CompilationUnit;

/**
 *
 * @author yur
 */
public final class SymbolLocation {
    public final CompilationUnit unit;
    public final int line, column;
    
    public SymbolLocation(CompilationUnit unit, int line, int col) {
        this.unit = unit;
        this.line = line;
        this.column = col;
    }
    
}
