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
public final class SymbolRedefinitionError extends RuntimeException {
    public SymbolRedefinitionError(SymItem sym) {
        super(sym.toString());
    }
}
