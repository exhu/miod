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
public final class SymbolDesc {

    final public SymbolLocation location;
    final public SymbolVisibility visibility;
    final public String name;
    public MiodType type;
    // no annotations to alias
    //final public List<MiodAnnotation> annotations;

    public SymbolDesc(String name, SymbolLocation location, MiodType type,
            SymbolVisibility visibility) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.visibility = visibility;
    }
}
