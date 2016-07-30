/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public final class AliasSymbol extends SymbolTableItem {
    public final SymbolTableItem aliasFor;

    public AliasSymbol(SymbolDesc desc, SymbolTableItem aliasFor) {
        super(desc);
        assert(desc.kind == SymbolKind.Alias);
        this.aliasFor = aliasFor;
    }

    @Override
    public SymbolTableItem resolveAlias() {
        return aliasFor.resolveAlias();
    }
}
