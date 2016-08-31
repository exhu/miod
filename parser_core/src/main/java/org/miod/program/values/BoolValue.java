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
public final class BoolValue extends MiodValue {
    public final boolean value;
    private BoolValue(boolean v) {
        super(PrimitiveType.BOOL);
        value = v;
    }

    public static final BoolValue TRUE = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);

}
