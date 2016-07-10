/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import java.util.List;
import org.miod.program.annotations.MiodAnnotation;

/**
 *
 * @author yur
 */
public abstract class SymbolTableItem {
    final public SymbolLocation location;
    final public SymbolVisibility visibility;
    final public SymbolKind kind;
    final public List<MiodAnnotation> annotations;
    final public String name;
    
    public SymbolTableItem(String name, SymbolLocation location, SymbolKind kind,
            List<MiodAnnotation> annotations, SymbolVisibility visibility) {
        this.name = name;
        this.location = location;
        this.kind = kind;
        this.annotations = annotations;
        this.visibility = visibility;
    }
}
