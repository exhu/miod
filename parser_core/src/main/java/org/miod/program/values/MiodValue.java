/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

import org.miod.program.symbol_table.SymbolType;

/** Values for consts and annotations.
 *
 * @author yur
 */
public abstract class MiodValue {
    protected SymbolType type;
    public MiodValue(SymbolType type) {
        this.type = type;
    }

}
