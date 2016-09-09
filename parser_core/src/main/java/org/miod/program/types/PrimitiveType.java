/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Base for standard types both public and implementation, e.g. WEAK refs, int8
 *
 * @author yur
 */
public final class PrimitiveType extends MiodType {
    private PrimitiveType(ValueTypeId typeId) {
        super(typeId);
    }
    
    public static final PrimitiveType BOOL = new PrimitiveType(ValueTypeId.BOOL);
    public static final PrimitiveType INT32 = new PrimitiveType(ValueTypeId.INT32);
    public static final PrimitiveType INT64 = new PrimitiveType(ValueTypeId.INT64);
    public static final PrimitiveType CARDINAL = new PrimitiveType(ValueTypeId.CARDINAL);

    public static PrimitiveType typeFromInteger(long v) {
        if (v <= Integer.MAX_VALUE) {
            if (v >= 0)
                return CARDINAL;
            if (v >= Integer.MIN_VALUE)
                return INT32;
        }
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

    public static boolean compatible(ValueTypeId a, ValueTypeId b) {
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
