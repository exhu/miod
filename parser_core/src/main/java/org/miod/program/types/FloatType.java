/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class FloatType extends NumericType<FloatType> {
    public final boolean doublePrecision;

    public final static FloatType FLOAT = new FloatType(false);
    public final static FloatType DOUBLE = new FloatType(true);

    @Override
    public FloatType promote(FloatType other) {
        if (other.doublePrecision) {
            return other;
        }
        return this;
    }

    private FloatType(boolean doublePrecision) {
        super(ValueTypeId.FLOAT);
        this.doublePrecision = doublePrecision;
    }

    private boolean isSameType(MiodType other) {
        return other instanceof FloatType
                && ((FloatType)other).doublePrecision == doublePrecision;
    }

    @Override
    public boolean supportsEqualOp(MiodType other) {
        return isSameType(other);
    }

    @Override
    public boolean supportsLessThanOp(MiodType other) {
        return isSameType(other);
    }

    @Override
    public boolean supportsPlusOp(MiodType other) {
        return isSameType(other);
    }
}
