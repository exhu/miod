/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.SymbolKind;
import org.miod.program.symbol_table.GlobalSymbolTable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.miod.parser.ErrorListener;
import org.miod.program.annotations.MiodAnnotation;

/**
 *
 * @author yur
 */
public final class CompilationUnit {
    public static final String UNIT_FILENAME_SUFFIX = ".miod";

    public final String filename;

    /// both import_all and import myunit::myProc etc.
    final private Set<CompilationUnit> importedUnits = new HashSet<>();
    final public GlobalSymbolTable symTable;

    final private List<MiodAnnotation> annotations = new ArrayList<>();

    /// line, col = place of 'unit' directive
    public CompilationUnit(String name, int line, int col, String filename,
            ErrorListener errorListener) {
        this.filename = filename;
        symTable = new GlobalSymbolTable(name, errorListener);
        symTable.put(new SymItem(name, SymbolKind.Unit, SymbolVisibility.Private, new SymbolLocation(this, line, col)));
    }

    /// Adds import into dependencies, symbol table
    public CompilationUnit addImport(String name, int line, int col,
            SymbolVisibility visibility,
            String filename, boolean fullNames, CompilationUnit newUnit) {
        //CompilationUnit newUnit = new CompilationUnit(name, line, col, filename);
        //SymLocation location = new SymbolLocation(this, line, col);
        //SymItem item = new SymItem(name, SymbolKind.Unit, visibility, location);
        //symTable.put(item);
        symTable.addImport(newUnit.symTable, fullNames);
        return newUnit;
    }
    
    public void addAliasToUnit(String name) {
        // TODO
        //SymItem item = new SymItem(name, SymbolKind.Unit, visibility, location);
        //symTable.put(item);
    }
}
