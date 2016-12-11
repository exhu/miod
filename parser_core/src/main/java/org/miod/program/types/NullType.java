/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class NullType extends MiodType {
    public static final NullType VALUE = new NullType();

    @Override
    public boolean isComparableTo(MiodType other) {
        // TODO reference types
        return other instanceof NullType
                || other instanceof ArrayRefType;
    }

    @Override
    public boolean supportsEqualOp(MiodType other) {
        switch(other.typeId) {
            case CLASS_REF:
            case CLASS_WEAK:
            case CLASS_WEAK_REF:
            case METHOD_WITH_INSTANCE_REF:
            case PROC_REF:
            case STRING:
                return true;
            default:;
        }
        return false;
    }

    private NullType() {
        super(ValueTypeId.NULL);
    }
}
