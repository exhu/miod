/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Argument to proc passed by reference
 *
 * @author yur
 */
public final class VarType extends StandardType {
    public final TypeSymbol ref;
    public VarType(TypeSymbol ref) {
        this.ref = ref;
    }
    
}
