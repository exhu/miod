/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import static org.miod.program.types.PrimitiveType.CARDINAL;
import static org.miod.program.types.PrimitiveType.INT32;
import static org.miod.program.types.PrimitiveType.INT64;
import static org.miod.program.types.PrimitiveType.UINT64;


/**
 *
 * @author yur
 */
public final class TypeUtils {
    private TypeUtils() {}

    public static PrimitiveType typeFromInteger(long v) {
        if (v <= Integer.MAX_VALUE) {
            if (v >= 0)
                return CARDINAL;
            if (v >= Integer.MIN_VALUE)
                return INT32;
        } else if (v > Long.MAX_VALUE)
            return UINT64;

        return INT64;
    }

    final public static ValueTypeId[] SIGNED_TYPES = new ValueTypeId[]{
        ValueTypeId.INT8, ValueTypeId.INT16,
        ValueTypeId.INT32, ValueTypeId.INT64 };

    final public static ValueTypeId[] UNSIGNED_TYPES = new ValueTypeId[]{
        ValueTypeId.UINT8, ValueTypeId.UINT16, ValueTypeId.CARDINAL,
        ValueTypeId.UINT32, ValueTypeId.UINT64  };

    private static boolean matchType(ValueTypeId t, ValueTypeId[] arr) {
        for(ValueTypeId i : arr) {
            if (i == t)
                return true;
        }
        return false;
    }

    public static boolean comparable(ValueTypeId a, ValueTypeId b) {
        if (a == b)
            return true;

        if (matchType(a, SIGNED_TYPES) && matchType(b, SIGNED_TYPES))
            return true;
        return matchType(a, UNSIGNED_TYPES) && matchType(b, UNSIGNED_TYPES);
    }

    public static ValueTypeId promote(ValueTypeId a, ValueTypeId b) {
        if (a.compareTo(b) > 0)
            return a;
        return b;
    }
}
