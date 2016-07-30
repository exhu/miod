/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.ProcBlock;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTable;

/** A proc declaration with body/statements in unit/class scope.
 *
 * @author yur
 */
public class ProcTypeWithBody extends ProcType {
    protected final ProcBlock block;
    public ProcTypeWithBody(SymbolTable parent, SymbolDesc desc, ErrorListener errorListener) {
        super(parent, desc, errorListener);
        block = new ProcBlock(this, errorListener);
    }
    
}
