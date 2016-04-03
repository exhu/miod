/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author yur
 */
public class CompilationUnit {
    public final String filename;
    
    /// both import_all and import myunit::myProc etc.
    final public Set<CompilationUnit> importedUnits = new HashSet<>();
    final public GlobalSymbolTable symTable;

    public CompilationUnit(String name, int line, int col, String filename) {
        this.filename = filename;
        symTable = new GlobalSymbolTable(name);
        symTable.put(new SymItem(name, SymKind.Unit, SymVisibility.Private, new SymLocation(this, line, col)));
    }    
}
