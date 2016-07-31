/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class ArrayType extends MiodType {
    public final int size;
    public final MiodType elementType;
    public ArrayType(int size, MiodType elementType) {
        super(ValueTypeId.ARRAY_SIZED);
        this.size = size;
        this.elementType = elementType.resolve();
    }
}
