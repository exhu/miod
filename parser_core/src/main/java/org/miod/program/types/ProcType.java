/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolWithSymTable;

/** A proc declaration in type section. Symbol table contains declared arguments.
 *
 * @author yur
 */
public class ProcType extends SymbolWithSymTable {
    public ProcType(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(parent, desc, errorListener);
    }
}
