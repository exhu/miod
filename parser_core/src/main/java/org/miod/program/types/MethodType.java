/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTable;

/**
 *
 * @author yur
 */
public final class MethodType extends ProcType {
    public MethodType(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(parent, desc, errorListener);
    }
    
}
