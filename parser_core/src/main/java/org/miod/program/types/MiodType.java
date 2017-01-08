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
    private final ValueTypeId typeId;
    public final boolean isReferenceType;
    
    protected MiodType(ValueTypeId typeId) {
        this.typeId = typeId;
        switch(typeId) {
            case NULL:
            case CLASS_INSTANCE:
            case METHOD_WITH_INSTANCE_REF:
            case PROC_REF:
            case STRING:
                isReferenceType = true;
                break;
            default:
                isReferenceType = false;
        }
    }

    /// returns final type in the chain, e.g. for UserTypes
    MiodType resolve() {
        return this;
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

    public boolean supportsCastTo(MiodType other) {
        return false;
    }
}
