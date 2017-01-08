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
public final class UnknownIdentifier extends CompilerError {
    private final String name;

    public UnknownIdentifier(SymbolLocation loc, String name) {
        super(loc);
        this.name = name;
    }
    
    @Override
    public String toString() {
        return makeErrorText(String.format("Unknown identifier '%s'", name));
    }
}
