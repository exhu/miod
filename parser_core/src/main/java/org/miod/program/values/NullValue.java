/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.NullType;

/**
 *
 * @author yur
 */
public final class NullValue extends MiodValue {
    public static final NullValue VALUE = new NullValue();
    private NullValue() {
        super(NullType.VALUE);
    }

}
