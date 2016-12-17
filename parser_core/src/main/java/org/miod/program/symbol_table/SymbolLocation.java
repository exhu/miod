/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public final class SymbolLocation {
    public final String unitPath;
    public final int line, column;
    
    public SymbolLocation(String unitPath, int line, int col) {
        this.unitPath = unitPath;
        this.line = line;
        this.column = col;
    }
    
}
