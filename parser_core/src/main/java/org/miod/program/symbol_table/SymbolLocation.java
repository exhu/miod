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
    public final String unitName;
    public final int line, column;
    
    public SymbolLocation(String unitName, int line, int col) {
        this.unitName = unitName;
        this.line = line;
        this.column = col;
    }
    
}
