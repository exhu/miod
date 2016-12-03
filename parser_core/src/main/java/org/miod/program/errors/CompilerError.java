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
public abstract class CompilerError {
    protected String text;
    protected SymbolLocation location;
    public CompilerError(String msg) {
        this.text = msg;
    }

}
