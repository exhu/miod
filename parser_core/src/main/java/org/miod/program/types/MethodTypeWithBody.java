/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.parser.ErrorListener;
import org.miod.program.SymbolTable;

/**
 *
 * @author yur
 */
public final class MethodTypeWithBody extends ProcTypeWithBody {
    public MethodTypeWithBody(SymbolTable parent, ErrorListener errorListener) {
        super(parent, errorListener);
    }
}
