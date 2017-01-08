/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class NullType extends MiodType {
    public static final NullType INSTANCE = new NullType();

    @Override
    public boolean supportsEqualOp(MiodType other) {
        return other.isReferenceType;
    }

    private NullType() {
        super(ValueTypeId.NULL);
    }
}
