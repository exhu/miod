/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.BoolType;

/**
 *
 * @author yur
 */
public final class BoolValue extends MiodValue implements EqualOp {
    public static final BoolValue TRUE = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);

    public static BoolValue fromBoolean(boolean b) {
        if (b == true)
            return TRUE;
        return FALSE;
    }

    @Override
    public BoolValue equal(EqualOp other) {
        return fromBoolean(((BoolValue)other).value == value);
    }

    public final boolean value;
    private BoolValue(boolean v) {
        super(BoolType.INSTANCE);
        value = v;
    }
}
