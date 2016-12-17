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
    public final long maxValue, minValue;

    public static final IntegerType INT8 = new IntegerType(ValueTypeId.INT8);
    public static final IntegerType UINT8 = new IntegerType(ValueTypeId.UINT8);
    public static final IntegerType INT16 = new IntegerType(ValueTypeId.INT16);
    public static final IntegerType UINT16 = new IntegerType(ValueTypeId.UINT16);
    public static final IntegerType INT32 = new IntegerType(ValueTypeId.INT32);
    public static final IntegerType UINT32 = new IntegerType(ValueTypeId.UINT32);
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

    public final boolean isInRange(long v) {
        return v >= minValue && v <= maxValue;
    }

    @Override
    public IntegerType promote(IntegerType other) {        
        if (signed == other.signed) {
            if (other.bits > bits)
                return other;
            else
                return this;
        }
        
        // cardinal        
        IntegerType card = null, noncard = null;

        if (typeId == ValueTypeId.CARDINAL) {
            card = this;
            noncard = other;
        } else if (other.typeId == ValueTypeId.CARDINAL) {
            card = other;
            noncard = this;
        }

        if (card != null) {
            if (noncard.bits < 32)
                return INT32;
            else
                return noncard;
        }

        return null;
    }

    private IntegerType(ValueTypeId typeId) {
        super(typeId);
        switch(typeId) {
            case INT8:
                bits = 8;
                signed = true;
                minValue = Byte.MIN_VALUE;
                maxValue = Byte.MAX_VALUE;
                break;
            case INT16:
                bits = 16;
                signed = true;
                minValue = Short.MIN_VALUE;
                maxValue = Short.MAX_VALUE;
                break;
            case INT32:
                bits = 32;
                signed = true;
                minValue = Integer.MIN_VALUE;
                maxValue = Integer.MAX_VALUE;
                break;
            case INT64:
                bits = 64;
                signed = true;
                minValue = Long.MIN_VALUE;
                maxValue = Long.MAX_VALUE;
                break;
            case UINT8:
                bits = 8;
                signed = false;
                minValue = 0;
                maxValue = 1 << 8 - 1;
                break;
            case UINT16:
                bits = 16;
                signed = false;
                minValue = 0;
                maxValue = 1 << 16 - 1;
                break;
            case UINT32:
                bits = 32;
                signed = false;
                minValue = 0;
                maxValue = 1 << 32 - 1;
                break;
            case UINT64:
                bits = 64;
                signed = false;
                minValue = 0;
                maxValue = Long.MAX_VALUE; // TODO fix
                break;
            case CARDINAL:
                bits = 31;
                signed = false;
                minValue = 0;
                maxValue = Integer.MAX_VALUE;
                break;
            default: 
                bits = 0;
                signed = false;
                minValue = 0;
                maxValue = 0;
        }
    }

    private boolean compatibleWith(MiodType other) {
        if (other instanceof IntegerType) {
            IntegerType otherInt = (IntegerType)other;
            return (signed == otherInt.signed) || (typeId == ValueTypeId.CARDINAL
                   || otherInt.typeId == ValueTypeId.CARDINAL);
        }
        return false;
    }

    @Override
    public boolean supportsEqualOp(MiodType other) {
        return compatibleWith(other);
    }

    @Override
    public boolean supportsLessThanOp(MiodType other) {
        return compatibleWith(other);
    }

    @Override
    public boolean supportsPlusOp(MiodType other) {
        return compatibleWith(other);
    }

    @Override
    public boolean supportsCastTo(MiodType other) {
        return other instanceof IntegerType;
    }
}
