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
    public final static FloatType FLOAT = new FloatType(ValueTypeId.FLOAT);
    public final static FloatType DOUBLE = new FloatType(ValueTypeId.DOUBLE);

    @Override
    public FloatType promote(FloatType other) {
        if (other.typeId == ValueTypeId.DOUBLE)
            return other;
        return this;
    }

    private FloatType(ValueTypeId typeId) {
        super(typeId);
    }
}
