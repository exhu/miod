/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class AliasedType extends MiodType {
    public final MiodType aliasFor;
    AliasedType(MiodType aliasFor, ValueTypeId typeId) {
        super(typeId);
        this.aliasFor = aliasFor;
    }
    
    /// pass through aliases
    @Override
    final public MiodType resolve() {
        return aliasFor.resolve();
    }
}
