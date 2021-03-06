/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.StringType;

/**
 *
 * @author yur
 */
public class StringValue extends MiodValue {
    public final String value;
    public StringValue(String v) {
        super(StringType.INSTANCE);
        value = v;
    }

}
