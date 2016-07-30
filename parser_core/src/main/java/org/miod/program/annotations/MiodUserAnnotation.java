/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public final class MiodUserAnnotation extends MiodAnnotation {
    private final MiodValue value;
    private final SymbolTableItem struct;

    public MiodUserAnnotation(MiodValue value, SymbolTableItem struct) {
        super(struct.desc.name);
        this.value = value;
        this.struct = struct;
    }

}
