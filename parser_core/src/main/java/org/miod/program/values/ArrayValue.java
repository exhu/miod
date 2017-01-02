/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.ArrayType;

/** Array literal [item, item, ... item]
 *
 * @author yur
 */
public final class ArrayValue extends MiodValue {
    public final MiodValue[] values;
    public ArrayValue(ArrayType t, MiodValue[] values) {
        super(t);
        this.values = values;
    }

}
