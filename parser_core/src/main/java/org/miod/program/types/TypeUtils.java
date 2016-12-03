/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import static org.miod.program.types.IntegerType.CARDINAL;
import static org.miod.program.types.IntegerType.INT32;
import static org.miod.program.types.IntegerType.INT64;


/**
 *
 * @author yur
 */
@Deprecated
public final class TypeUtils {
    private TypeUtils() {}

    @Deprecated
    public static IntegerType typeFromInteger(long v) {
        if (v <= Integer.MAX_VALUE) {
            if (v >= 0)
                return CARDINAL;
            else if (v >= Integer.MIN_VALUE)
                return INT32;
        }
        /*else if (v > Long.MAX_VALUE) -- TODO replace long with BigInt?
            return UINT64;*/

        return INT64;
    }

    @Deprecated
    final public static ValueTypeId[] SIGNED_TYPES = new ValueTypeId[]{
        ValueTypeId.INT8, ValueTypeId.INT16,
        ValueTypeId.INT32, ValueTypeId.INT64 };

    @Deprecated
    final public static ValueTypeId[] UNSIGNED_TYPES = new ValueTypeId[]{
        ValueTypeId.UINT8, ValueTypeId.UINT16, ValueTypeId.CARDINAL,
        ValueTypeId.UINT32, ValueTypeId.UINT64  };

    @Deprecated
    private static boolean matchType(ValueTypeId t, ValueTypeId[] arr) {
        for(ValueTypeId i : arr) {
            if (i == t)
                return true;
        }
        return false;
    }

    @Deprecated
    private static boolean isSignedInt(ValueTypeId a) {
        return matchType(a, SIGNED_TYPES);
    }

    @Deprecated
    private static boolean isUnsignedInt(ValueTypeId a) {
        return matchType(a, UNSIGNED_TYPES);
    }

    @Deprecated
    private static boolean isInteger(ValueTypeId a) {
        return isSignedInt(a) || isUnsignedInt(a);
    }    

    public static class PromotionResult {
        public ValueTypeId typeId;
    }

    @Deprecated
    private static boolean tryPromoteCardinal(ValueTypeId a, ValueTypeId b, PromotionResult out) {
        if (a == ValueTypeId.CARDINAL && isSignedInt(b)) {
            // <= int32  -> int32, else b
            if (b.compareTo(ValueTypeId.INT32) < 0) {
                out.typeId = ValueTypeId.INT32;
            } else {
                out.typeId = b;
            }
            return true;
        }

        return false;
    }

    @Deprecated
    private static ValueTypeId promoteInteger(ValueTypeId a, ValueTypeId b) {
        if (a == b) {
            return a;
        }

        PromotionResult result = new PromotionResult();
        if (tryPromoteCardinal(a, b, result)) {
            return result.typeId;
        } else if (tryPromoteCardinal(b, a, result)) {
            return result.typeId;
        }

        // TODO uint64 support
        return ValueTypeId.INT64;
    }

    public static boolean isFloatOrDouble(ValueTypeId a) {
        return a == ValueTypeId.FLOAT || a == ValueTypeId.DOUBLE;
    }

    @Deprecated
    public static boolean promote(ValueTypeId a, ValueTypeId b, PromotionResult out) {
        if (a == ValueTypeId.FLOAT || b == ValueTypeId.FLOAT) {
            out.typeId = ValueTypeId.FLOAT;
            return true;
        } else if (isFloatOrDouble(a) && isFloatOrDouble(b)) {
            out.typeId = ValueTypeId.DOUBLE;
            return true;
        } else if (isInteger(a) && isInteger(b)) {
            out.typeId = promoteInteger(a, b);
            return true;
        }
        return false;
    }
}
