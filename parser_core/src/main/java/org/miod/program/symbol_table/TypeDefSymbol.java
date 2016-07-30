/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/** DEPRECATED. Distinct type. E.g. type int = int32
 *
 * @author yur
 */
public final class TypeDefSymbol extends AliasSymbol {
    public TypeDefSymbol(SymbolDesc desc, SymbolTableItem aliasFor) {
        super(desc, aliasFor);
        throw new UnsupportedOperationException("todo remove");
    }

}
