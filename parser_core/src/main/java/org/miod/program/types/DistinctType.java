/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.SymItem;

/** type mytype = othertype
 * 
 * @author yur
 */
public final class DistinctType extends AliasedType {
    DistinctType(SymItem aliasFor) {
        super(aliasFor);
    }
    
    @Override
    public boolean isAliasedType() {
        return aliasFor.type.isAliasedType();
    }
}
