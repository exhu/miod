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
    final public SymLocation location;
    /// type can by null!
    public TypeSymbol type;
    
    public SymItem(String name, SymKind kind, SymLocation location) {
        this.name = name;
        this.kind = kind;
        this.location = location;
    }
}
