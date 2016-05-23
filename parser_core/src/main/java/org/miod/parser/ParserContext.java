/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.miod.program.CompilationUnit;
import org.miod.program.SymItem;

/** Maintains all processed units, predefined directives, options etc.
 *
 * @author yur
 */
public final class ParserContext {
    private Set<CompilationUnit> units = new HashSet<>();
    private Set<String> packagePaths = new HashSet<>();
    private Map<String, SymItem> globalDefines = new HashMap<>();
    
    public ParserContext(List<String> packagePaths, Map<String, String> defines) {
        this.packagePaths.addAll(packagePaths);
        evalDefines(defines);
    }
    
    public CompilationUnit parseUnit(String fn) {        
        // TODO
        return null;
    }
    
    private void evalDefines(Map<String, String> defines) {
        // TODO convert Strings to SymItems
    }
}
