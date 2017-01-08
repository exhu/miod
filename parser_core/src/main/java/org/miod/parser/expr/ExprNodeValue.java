/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.miod.program.values.BoolValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;

/**
 *
 * @author yur
 */
public final class ExprNodeValue extends ExprNodeData {
    public final MiodValue value;

    public static ExprNodeValue newValue(MiodValue v) {
        if (v == NullValue.INSTANCE) {
            return VALUE_NULL;
        }
        if (v == BoolValue.FALSE) {
            return VALUE_FALSE;
        }
        if (v == BoolValue.TRUE) {
            return VALUE_TRUE;
        }

        return new ExprNodeValue(v);
    }

    private final static ExprNodeValue VALUE_NULL = new ExprNodeValue(NullValue.INSTANCE);
    private final static ExprNodeValue VALUE_FALSE = new ExprNodeValue(BoolValue.FALSE);
    private final static ExprNodeValue VALUE_TRUE = new ExprNodeValue(BoolValue.TRUE);

    private ExprNodeValue(MiodValue v) {
        this.value = v;
    }

}
