/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.symbol_table.SymbolTableItem;

/** Type that refers to symbol table eg alias, *should not be used!*.
 *
 * @author yur
 */
public final class UserType extends MiodType {
    public SymbolTableItem symbol;
    public UserType(ValueTypeId typeId, SymbolTableItem symbol) {
        super(typeId);
        this.symbol = symbol;
    }

    @Override
    public MiodType resolve() {
        return symbol.resolveAlias().desc.type.resolve();
    }

}
