/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTableItem;

/**
 *
 * @author yur
 */
public final class AliasSymbol extends SymbolTableItem {
    public final SymbolTableItem aliasFor;

    public AliasSymbol(SymbolDesc desc, SymbolTableItem aliasFor) {
        super(desc);
        this.aliasFor = aliasFor;
        if (aliasFor != null) {
            this.desc.type = aliasFor.resolveAlias().desc.type;
        }
    }

    @Override
    final public SymbolTableItem resolveAlias() {
        return aliasFor.resolveAlias();
    }
}
