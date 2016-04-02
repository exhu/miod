/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/** Base class for structs, classes, arrays...
 *
 * @author yur
 */
public abstract class TypeSymbol {
    public boolean isAliasedType() {
        return false;
    }
    
    public boolean isSymbolTable() {
        return false;
    }    
}
