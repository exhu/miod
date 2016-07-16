/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class EnumValueType extends MiodType {
    final EnumType enumType;

    public EnumValueType(EnumType enumType) {
        super(ValueTypeId.ENUM_VALUE);
        this.enumType = enumType;
    }

}
