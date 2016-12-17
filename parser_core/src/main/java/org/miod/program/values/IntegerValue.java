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
public final class IntegerValue extends MiodValue implements LessThanOp,
        EqualOp, PlusOp {

    public final long value;

    public IntegerValue(long v) {
        super(IntegerType.fromLiteral(v));
        value = v;
    }

    private IntegerValue(long v, MiodType t) {
        super(t);
        value = v;
    }

    /// Tries to convert, if the value cannot fit the target
    /// returns a bigger type.
    public IntegerValue convertTo(IntegerType target) {
        if (type.typeId == target.typeId) {
            return this;
        }

        if (target.isInRange(value)) {
            return new IntegerValue(value, target);
        }

        return new IntegerValue(value, ((IntegerType) type).promote(target));
    }

    /// Tries to convert preserving the value, otherwise rounds to the lowest
    /// or highest value of the target type.
    public IntegerValue castConvert(IntegerType target) {
        if (type.typeId == target.typeId) {
            return this;
        }

        if (target.isInRange(value)) {
            return new IntegerValue(value, target);
        }

        return new IntegerValue(value > target.maxValue ? target.maxValue
                : target.minValue, target);
    }

    @Override
    public MiodValue castTo(MiodType other) {
        return castConvert((IntegerType)other);
    }

    @Override
    public BoolValue lessThan(LessThanOp other) {
        IntegerValue o = (IntegerValue) other;
        return BoolValue.fromBoolean(value < o.value);
    }

    @Override
    public BoolValue lessThanOrEqual(LessThanOp other) {
        IntegerValue o = (IntegerValue) other;
        return BoolValue.fromBoolean(value <= o.value);
    }

    @Override
    public BoolValue equal(EqualOp other) {
        return BoolValue.fromBoolean(value == ((IntegerValue) other).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public MiodValue plusOp(MiodValue other) {
        return new IntegerValue(value + ((IntegerValue)other).value, 
                ((IntegerType)type).promote((IntegerType)other.type));
    }


}
