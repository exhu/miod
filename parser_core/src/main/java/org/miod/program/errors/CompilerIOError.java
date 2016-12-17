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
public final class CompilerIOError extends CompilerError {
    final String fileName;
    public CompilerIOError(String fileName) {
        super(null);
        this.fileName = fileName;
    }

}
