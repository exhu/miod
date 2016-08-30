/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.MiodType;

/** Values for consts and annotations.
 *
 * @author yur
 */
public abstract class MiodValue {
    protected final MiodType type;

    final public MiodType getType() {
        return type;
    }
    public MiodValue(MiodType type) {
        this.type = type;
    }

}
