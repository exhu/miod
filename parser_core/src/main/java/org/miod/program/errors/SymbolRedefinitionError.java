/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

import org.miod.program.SymItem;

/**
 *
 * @author yur
 */
public final class SymbolRedefinitionError extends CompilerError {
    public SymbolRedefinitionError(SymItem sym) {
        // TODO error message
        super(sym.toString());
    }
}
