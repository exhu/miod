/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import org.miod.program.types.TypeSymbol;

/**
 *
 * @author yur
 */
public final class SymItem {
    final public String name;
    final public SymKind kind;
    /// null for predefined consts
    final public SymLocation location;
    /// type can by null for first pass!
    public TypeSymbol type;
    final public SymVisibility visibility;
    /// value for consts
    public Object value;
    
    public SymItem(String name, SymKind kind, SymVisibility visibility, SymLocation location) {
        this.name = name;
        this.kind = kind;
        this.location = location;
        this.visibility = visibility;
    }
}
