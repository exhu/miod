/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/** type mytype = othertype
 * 
 * @author yur
 */
public final class TypeDistinct extends AliasedType {
    TypeDistinct(SymItem aliasFor) {
        super(aliasFor);
    }
    
    @Override
    public boolean isAliasedType() {
        return aliasFor.type.isAliasedType();
    }
}
