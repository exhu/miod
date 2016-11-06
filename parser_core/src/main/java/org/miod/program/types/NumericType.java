/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class NumericType extends MiodType {
    public NumericType(ValueTypeId typeId) {
        super(typeId);
    }

    @Override
    public boolean supportsEqualOp() {
        return true;
    }

    @Override
    public boolean supportsLessThanOp() {
        return true;
    }
}
