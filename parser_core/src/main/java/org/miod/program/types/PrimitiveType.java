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


    public static PrimitiveType typeFromInteger(long v) {
        if (v >= Integer.MIN_VALUE && v <= Integer.MAX_VALUE) {
            return INT32;
        }
        return INT64;
    }
}
