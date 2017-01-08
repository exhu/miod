/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

import org.miod.program.values.BoolValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;

/**
 *
 * @author yur
 */
public final class MiodNodeValue extends MiodNodeData {
    public final MiodValue value;

    public static MiodNodeValue newValue(MiodValue v) {
        if (v == NullValue.INSTANCE) {
            return VALUE_NULL;
        }
        if (v == BoolValue.FALSE) {
            return VALUE_FALSE;
        }
        if (v == BoolValue.TRUE) {
            return VALUE_TRUE;
        }

        return new MiodNodeValue(v);
    }

    private final static MiodNodeValue VALUE_NULL = new MiodNodeValue(NullValue.INSTANCE);
    private final static MiodNodeValue VALUE_FALSE = new MiodNodeValue(BoolValue.FALSE);
    private final static MiodNodeValue VALUE_TRUE = new MiodNodeValue(BoolValue.TRUE);

    private MiodNodeValue(MiodValue v) {
        this.value = v;
    }

}
