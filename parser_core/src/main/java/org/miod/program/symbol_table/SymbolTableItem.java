/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.program.types.MiodType;

/**
 *
 * @author yur
 */
public abstract class SymbolTableItem {

    final public SymbolLocation location;
    final public SymbolVisibility visibility;
    final public SymbolKind kind;
    final public String name;
    final public MiodType type;
    // no annotations to alias
    //final public List<MiodAnnotation> annotations;

    public SymbolTableItem(String name, SymbolLocation location, SymbolKind kind,
            SymbolVisibility visibility) {
        this.name = name;
        this.location = location;
        this.kind = kind;        
        this.visibility = visibility;
    }

    public SymbolTableItem resolveAlias() {
        return this;
    }
}
