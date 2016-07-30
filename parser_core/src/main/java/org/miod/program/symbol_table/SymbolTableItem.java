/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public abstract class SymbolTableItem {
    final public SymbolDesc desc;
    
    // no annotations to alias
    //final public List<MiodAnnotation> annotations;

    public SymbolTableItem(SymbolDesc desc) {
        this.desc = desc;
    }

    public SymbolTableItem resolveAlias() {
        return this;
    }
}
