/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class MiodType {
    public final ValueTypeId typeId;
    protected MiodType(ValueTypeId typeId) {
        this.typeId = typeId;
    }

    /// returns final type in the chain, e.g. for UserTypes
    MiodType resolve() {
        return this;
    }

    @Deprecated
    public boolean isComparableTo(MiodType other) {
        return false;
    }
    
    public boolean supportsEqualOp(MiodType other) {
        return false;
    }
    
    public boolean supportsLessThanOp(MiodType other) {
        return false;
    }

    public boolean supportsPlusOp(MiodType other) {
        return false;
    }
}
