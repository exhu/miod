/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.Path;
import org.miod.program.CompilationUnit;

/** Useless? ParserContext is used to parse
 *
 * @author yur
 */
public final class UnitParser {    
    public UnitParser(ParserContext ctx) {
        this.context = ctx;        
    }
    
    public CompilationUnit parseFile(Path f, ErrorListener elistener) {
        // TODO
        return null;
    }
    
    private ParserContext context;
    private boolean terminateOnFirstError = false;
}
