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

    public CompilationUnit(String filename, String name) {
        this.filename = filename;
        this.name = name;
    }    
}
