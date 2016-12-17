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
public final class SyntaxError extends CompilerError {
    private String text;
    public SyntaxError(String txt, SymbolLocation loc) {
        super(loc);
        text = txt;
    }

    @Override
    public String toString() {
        return makeErrorText(text);
    }


}
