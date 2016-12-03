/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.BoolType;
import org.miod.program.types.MiodType;

/** Unknown value e.g. for function calls, variables etc.
 *
 * @author yur
 */
public final class RuntimeValue extends MiodValue {
    // TODO introduce static consts for default types
    public static final RuntimeValue BOOL = new RuntimeValue(BoolType.INSTANCE);

    private RuntimeValue(MiodType type) {
        super(type);
    }

}
