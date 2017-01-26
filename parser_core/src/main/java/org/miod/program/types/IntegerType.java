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
    public final boolean isCardinal;

    public static final IntegerType INT8 = new IntegerType(true, 8);
    public static final IntegerType UINT8 = new IntegerType(false, 8);
    public static final IntegerType INT16 = new IntegerType(true, 16);
    public static final IntegerType UINT16 = new IntegerType(false, 16);
    public static final IntegerType INT32 = new IntegerType(true, 32);
    public static final IntegerType UINT32 = new IntegerType(false, 32);
    public static final IntegerType INT64 = new IntegerType(true, 64);
    //public static final IntegerType UINT64 = new IntegerType(false, 64);
    public static final IntegerType CARDINAL = new IntegerType(false, 31);

    public static IntegerType fromLiteral(long v) {
        if (v <= Integer.MAX_VALUE) {
            if (v >= 0) {
                return CARDINAL;
            } else if (v >= Integer.MIN_VALUE) {
                return INT32;
            } else {
                return INT64;
            }
        }

        // TODO replace long with BigInt?
        return INT64;
    }

    public final boolean isInRange(long v) {
        return v >= minValue && v <= maxValue;
    }

    @Override
    public IntegerType promote(IntegerType other) {
        if (signed == other.signed) {
            if (other.bits > bits) {
                return other;
            } else {
                return this;
            }
        }

        // cardinal        
        IntegerType card = null, noncard = null;

        if (isCardinal) {
            card = this;
            noncard = other;
        } else if (other.isCardinal) {
            card = other;
            noncard = this;
        }

        if (card != null) {
            if (noncard.bits < 32) {
                return INT32;
            } else {
                return noncard;
            }
        }

        return null;
    }

    private IntegerType(boolean signed, int bits) {
        super(ValueTypeId.INTEGER);
        this.signed = signed;
        this.bits = bits;
        this.isCardinal = (bits == 31) && (signed == false);

        assert (bits <= 64 && bits >= 8);

        if (signed) {
            maxValue = (1L << (bits - 1)) - 1L;
            minValue = -(1L << (bits - 1));
        } else {
            minValue = 0;
            maxValue = (1L << bits) - 1L;
        }
    }

    private boolean compatibleWith(MiodType other) {
        if (other instanceof IntegerType) {
            IntegerType otherInt = (IntegerType) other;
            return (signed == otherInt.signed) || (isCardinal
                    || otherInt.isCardinal);
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

    @Override
    public boolean supportsMulOp(MiodType other) {
        return true;
    }
    
    
}
