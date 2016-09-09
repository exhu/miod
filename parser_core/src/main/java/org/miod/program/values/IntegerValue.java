/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.PrimitiveType;

/**
 *
 * @author yur
 */
public final class IntegerValue extends MiodValue implements GreaterThanOp {
    public final long value;
    public IntegerValue(long v) {
        super(PrimitiveType.typeFromInteger(v));
        value = v;
    }

    @Override
    public boolean greaterThan(GreaterThanOp other) {
        IntegerValue o = (IntegerValue)other;
        return value > o.value;
    }



}
