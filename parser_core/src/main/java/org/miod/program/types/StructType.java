/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.SymbolWithSymTable;

/**
 *
 * @author yur
 */
public final class StructType extends SymbolWithSymTable {
    public StructType(SymbolTable parent, ErrorListener errorListener) {
        super(parent, errorListener);
    }
    
}
