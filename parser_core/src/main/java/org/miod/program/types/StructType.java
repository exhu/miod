/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolWithSymTable;

/**
 *
 * @author yur
 */
public final class StructType extends SymbolWithSymTable {
    public StructType(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(parent, desc, errorListener);
    }
    
}
