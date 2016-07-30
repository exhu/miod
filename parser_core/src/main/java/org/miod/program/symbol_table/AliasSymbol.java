/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public class AliasSymbol extends SymbolTableItem {
    public final SymbolTableItem aliasFor;

    public AliasSymbol(SymbolDesc desc, SymbolTableItem aliasFor) {
        super(desc);        
        this.aliasFor = aliasFor;
    }

    @Override
    final public SymbolTableItem resolveAlias() {
        return aliasFor.resolveAlias();
    }
}
