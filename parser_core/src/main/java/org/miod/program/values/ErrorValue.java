/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

/**
 *
 * @author yur
 */
public final class ErrorValue extends MiodValue {
    public final String text;

    public static final ErrorValue UNDEFINED = new ErrorValue("Undefined identifier");
    public static final ErrorValue UNSUPPORTED = new ErrorValue("Unsupported operation");
    public static final ErrorValue TYPES_MISMATCH = new ErrorValue("Types mismatch");    

    private ErrorValue(String msg) {
        super(null);
        text = msg;
    }

}
