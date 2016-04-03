/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.SymItem;

/**
 *
 * @author yur
 */
public abstract class AliasedType extends TypeSymbol {
    public final SymItem aliasFor;
    AliasedType(SymItem aliasFor) {
        this.aliasFor = aliasFor;
    }
    
    /// pass through aliases
    final public SymItem getFinalSymbol() {
        if (aliasFor.type.isAliasedType())
            return ((AliasedType)aliasFor.type).getFinalSymbol();
        
        return aliasFor;
    }
    
    @Override
    public boolean isAliasedType() {
        return true;
    }
}
