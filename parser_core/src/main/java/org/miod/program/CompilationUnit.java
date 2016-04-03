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
    public final String name;
    
    /// both import_all and import myunit::myProc etc.
    final private Set<CompilationUnit> importedUnits = new HashSet<>();
    final private GlobalSymbolTable symTable;

    public CompilationUnit(String filename, String name, int line, int col) {
        this.filename = filename;
        this.name = name;
        symTable = new GlobalSymbolTable(name);
        symTable.put(new SymItem(name, SymKind.Unit, new SymLocation(this, line, col)));
    }    
}
