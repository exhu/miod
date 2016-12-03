/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class IntegerType extends NumericType<IntegerType> {
    public final int bits;
    public final boolean signed;

    public static final IntegerType INT8 = new IntegerType(ValueTypeId.INT8);
    public static final IntegerType INT32 = new IntegerType(ValueTypeId.INT32);
    public static final IntegerType INT64 = new IntegerType(ValueTypeId.INT64);
    public static final IntegerType UINT64 = new IntegerType(ValueTypeId.UINT64);
    public static final IntegerType CARDINAL = new IntegerType(ValueTypeId.CARDINAL);

    public static IntegerType fromLiteral(long v) {
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

    @Override
    public boolean isComparableTo(MiodType other) {
        if (other instanceof IntegerType) {
            IntegerType otherInt = (IntegerType)other;
            return (signed == otherInt.signed) || (typeId == ValueTypeId.CARDINAL
                   || otherInt.typeId == ValueTypeId.CARDINAL);
        }
        return false;
    }

    @Override
    public IntegerType promote(IntegerType other) {
        if (other.bits > bits)
            return other;
        return this;
    }

    private IntegerType(ValueTypeId typeId) {
        super(typeId);
        switch(typeId) {
            case INT8:
                bits = 8;
                signed = true;
                break;
            case INT16:
                bits = 16;
                signed = true;
                break;
            case INT32:
                bits = 32;
                signed = true;
                break;
            case INT64:
                bits = 64;
                signed = true;
                break;
            case UINT8:
                bits = 8;
                signed = false;
                break;
            case UINT16:
                bits = 16;
                signed = false;
                break;
            case UINT32:
                bits = 32;
                signed = false;
                break;
            case UINT64:
                bits = 64;
                signed = false;
                break;
            case CARDINAL:
                bits = 31;
                signed = false;
                break;
            default: 
                bits = 0;
                signed = false;
        }
    }

}
