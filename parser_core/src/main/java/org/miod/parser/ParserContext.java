/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.util.HashMap;
import java.util.Map;
import org.miod.program.CompilationUnit;

/**
 * Maintains all processed units, predefined directives, options etc.
 *
 * @author yur
 */
public final class ParserContext {

    private Map<String, CompilationUnit> units = new HashMap<>();    
    private UnitParserProvider parserProvider;

    /* Global defines are bad thing
    private Map<String, SymItem> globalDefines = new HashMap<>();
    better way is to have a miod.compiler.config module generated by
    compiler containing platform constants, e.g. debug mode or ref counting,
    cpu etc.
     */
    public ParserContext(UnitParserProvider provider) {
        this.parserProvider = provider;        
    }
    
    /// unitName = import directive argument e.g. miod::system
    public CompilationUnit parseUnit(String unitName) {
        CompilationUnit requestedUnit = units.get(unitName);
        if (requestedUnit == null) {
            requestedUnit = parserProvider.parseUnit(unitName);
        }
        return requestedUnit;
    }
}
