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
    public static final NullType VALUE = new NullType();

    @Override
    public boolean isComparableTo(MiodType other) {
        // TODO reference types
        return other instanceof NullType
                || other instanceof ArrayRefType;
    }

    @Override
    public boolean supportsEqualOp() {
        return true;
    }

    private NullType() {
        super(ValueTypeId.NULL);
    }
}
