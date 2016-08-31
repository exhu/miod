/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public final class UnitNotFoundError extends CompilerError {
    public UnitNotFoundError(String unitName) {
        super(String.format("Unit '%s' not found.", unitName));
    }
}
