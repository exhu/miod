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
    public MiodType(ValueTypeId typeId) {
        this.typeId = typeId;
    }

    /// returns final type in the chain, e.g. for UserTypes
    MiodType resolve() {
        return this;
    }

    boolean isCompatibleWith(MiodType other) {
        return other.typeId == typeId;
    }

    /// returns other if its range is greater than this,
    /// e.g. float.promote(double) -> double
    /// int32.promote(int16) -> int32
    MiodType promote(MiodType other) {
        return this;
    }

    public boolean supportsEqualOp() {
        return false;
    }
    
    boolean supportsLessThanOp() {
        return false;
    }
}
