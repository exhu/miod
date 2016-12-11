/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class BoolType extends MiodType {
    public static final BoolType INSTANCE = new BoolType();

    @Override
    public boolean supportsEqualOp(MiodType other) {
        return other.typeId == ValueTypeId.BOOL;
    }

    private BoolType() {
        super(ValueTypeId.BOOL);
    }
}