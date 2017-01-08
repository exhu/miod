/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class StringType extends MiodType {
    public static StringType INSTANCE = new StringType();

    private StringType() {
        super(ValueTypeId.STRING);
    }
}
