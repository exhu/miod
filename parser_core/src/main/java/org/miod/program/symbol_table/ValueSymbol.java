/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import java.util.List;
import org.miod.program.annotations.MiodAnnotation;
import org.miod.program.values.MiodValue;

/** Const or var.
 *
 * @author yur
 */
public final class ValueSymbol extends SymbolTableItem {
    public SymbolType type;
    public MiodValue data;

    public ValueSymbol(String name, SymbolLocation location, SymbolKind kind,
            List<MiodAnnotation> annotations, SymbolVisibility visibility) {
        super(name, location, kind, annotations, visibility);
    }
    

}
