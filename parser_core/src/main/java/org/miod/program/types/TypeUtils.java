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
public final class TypeUtils {
    private TypeUtils() {}

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

    private static boolean isSignedInt(ValueTypeId a) {
        return matchType(a, SIGNED_TYPES);
    }

    private static boolean isUnsignedInt(ValueTypeId a) {
        return matchType(a, UNSIGNED_TYPES);
    }

    private static boolean isInteger(ValueTypeId a) {
        return isSignedInt(a) || isUnsignedInt(a);
    }

    public static boolean isComparable(ValueTypeId a, ValueTypeId b) {
        if (isSignedInt(a) && isSignedInt(b))
            return true;
        else if (isUnsignedInt(a) && isUnsignedInt(b))
            return true;

        if (a == ValueTypeId.CARDINAL && isInteger(b))
            return true;
        else if (b == ValueTypeId.CARDINAL && isInteger(a))
            return true;

        if (a == b && isFloatOrDouble(a)) {
            return true;
        }

        return false;
    }

    public static class PromotionResult {
        public ValueTypeId typeId;
    }

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
