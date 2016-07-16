/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/** Symbol table types.
 *
 * @author yur
 */
public enum SymbolKind {
    Alias,
    Unit, // for imports
    Proc,
    Method,
    Type,  
    Var,
    Const
    //Annotation, -- annotations are parsed and applied to the annotated item    
}
