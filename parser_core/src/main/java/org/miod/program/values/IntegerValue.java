/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.IntegerType;

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

    @Override
    public boolean lessThan(LessThanOp other) {
        IntegerValue o = (IntegerValue)other;
        return value < o.value;
    }

    @Override
    public boolean lessThanOrEqual(LessThanOp other) {
        IntegerValue o = (IntegerValue)other;
        return value <= o.value;
    }

    @Override
    public boolean equal(EqualOp other) {
        return value == ((IntegerValue)other).value;
    }


}
