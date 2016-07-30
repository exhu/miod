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
    final public String canonicalName; // name with full module namespace etc.
    final public MiodType type;
    // no annotations to alias
    //final public List<MiodAnnotation> annotations;

    public SymbolTableItem(String name, String canonicalName,
            SymbolLocation location, SymbolKind kind, MiodType type,
            SymbolVisibility visibility) {
        this.name = name;
        this.canonicalName = canonicalName;
        this.location = location;
        this.kind = kind;
        this.type = type;
        this.visibility = visibility;
    }

    public SymbolTableItem resolveAlias() {
        return this;
    }
}
