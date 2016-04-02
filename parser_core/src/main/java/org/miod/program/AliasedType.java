/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program;

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
    final SymItem getFinalSymbol() {
        if (aliasFor.type.isAliasedType())
            return ((AliasedType)aliasFor.type).getFinalSymbol();
        
        return aliasFor;
    }
    
    @Override
    public boolean isAliasedType() {
        return true;
    }
}
