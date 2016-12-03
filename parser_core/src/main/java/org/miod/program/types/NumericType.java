/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class NumericType<T extends NumericType> extends MiodType {
    protected NumericType(ValueTypeId typeId) {
        super(typeId);
    }

    /// for arithmetic operations
    public abstract T promote(T other);

    @Override
    public boolean supportsEqualOp() {
        return true;
    }

    @Override
    public boolean supportsLessThanOp() {
        return true;
    }
}
