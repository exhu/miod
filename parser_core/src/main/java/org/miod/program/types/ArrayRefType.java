/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public class ArrayRefType extends MiodType {
    public final MiodType elementType;

    // TODO cache for standard types
    public static ArrayRefType fromMiodType(MiodType t) {
        return new ArrayRefType(t);
    }

    private ArrayRefType(MiodType elementType) {
        super(ValueTypeId.ARRAY_REF);
        this.elementType = elementType.resolve();
    }
}
