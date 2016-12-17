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

    protected final SymbolLocation location;

    public CompilerError(SymbolLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        if (location != null) {
            return String.format("%s, at '%s' %d:%d", this.getClass().getCanonicalName(),
                    location.unitPath, location.line, location.column);
        }
        return getClass().getCanonicalName();
    }

}
