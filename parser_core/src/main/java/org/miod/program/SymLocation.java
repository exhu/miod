/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/**
 *
 * @author yur
 */
public final class SymLocation {
    public final CompilationUnit unit;
    public final int line, column;
    
    public SymLocation(CompilationUnit unit, int line, int col) {
        this.unit = unit;
        this.line = line;
        this.column = col;
    }
    
}
