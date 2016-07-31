/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Sized array definition, e.g. type myarray = array[3, int], or var a : array[3]
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
