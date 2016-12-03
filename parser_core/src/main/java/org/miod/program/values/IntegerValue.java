/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;

/**
 *
 * @author yur
 */
public final class IntegerValue extends MiodValue implements LessThanOp, EqualOp {

    public final long value;

    public IntegerValue(long v) {
        super(IntegerType.fromLiteral(v));
        value = v;
    }

    private IntegerValue(long v, MiodType t) {
        super(t);
        value = v;
    }

    public IntegerValue convertTo(IntegerType target) {
        if (type.typeId == target.typeId) {
            return this;
        }

        return new IntegerValue(value, ((IntegerType) type).promote(target));
    }

    @Override
    public boolean lessThan(LessThanOp other) {
        IntegerValue o = (IntegerValue) other;
        return value < o.value;
    }

    @Override
    public boolean lessThanOrEqual(LessThanOp other) {
        IntegerValue o = (IntegerValue) other;
        return value <= o.value;
    }

    @Override
    public boolean equal(EqualOp other) {
        return value == ((IntegerValue) other).value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
