/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.types.TypeSymbol;

/** Values for consts and annotations.
 *
 * @author yur
 */
public abstract class MiodValue {
    protected TypeSymbol type;
    public MiodValue(TypeSymbol type) {
        this.type = type;
    }

}
