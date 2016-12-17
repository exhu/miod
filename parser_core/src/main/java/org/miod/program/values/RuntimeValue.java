/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.BoolType;
import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;

/** Unknown value e.g. for function calls, variables etc.
 *
 * @author yur
 */
public final class RuntimeValue extends MiodValue {
    // TODO introduce static consts for default types
    public static final RuntimeValue BOOL = new RuntimeValue(BoolType.INSTANCE);
    public static final RuntimeValue CARDINAL = new RuntimeValue(IntegerType.CARDINAL);

    public static RuntimeValue fromType(MiodType t) {        
        switch(t.typeId) {
            case CARDINAL:
                return RuntimeValue.CARDINAL;
            case BOOL:
                return RuntimeValue.BOOL;

            default:;
        }        
        return null;
    }

    private RuntimeValue(MiodType type) {
        super(type);
    }

    @Override
    public MiodValue castTo(MiodType other) {
        if (type.supportsCastTo(other)) {
            return fromType(other);
        }
        return null;
    }


}
