/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.SymbolKind;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.types.TypeSymbol;
import org.miod.program.values.MiodValue;

/** TODO split into TypeDefSymbol and ValueSymbol
 * TODO deprecate
 * @author yur
 */
public final class SymItem {
    final public String name;
    final public String fullName;
    final public SymbolKind kind;
    /// null for predefined consts
    final public SymbolLocation location;
    /// type can by null for first pass!
    public TypeSymbol type;
    final public SymbolVisibility visibility;
    /// value for consts
    public MiodValue value;


    
    public SymItem(String fullName, SymbolKind kind, SymbolVisibility visibility, SymbolLocation location) {
        this.fullName = fullName;
        this.name = SymbolTable.nameFromFullName(fullName);
        this.kind = kind;
        this.location = location;
        this.visibility = visibility;
    }
}
