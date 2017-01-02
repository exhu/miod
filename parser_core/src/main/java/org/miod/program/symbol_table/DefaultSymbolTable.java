/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.symbols.TypeDefSymbol;
import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;

/** Defines language builtin types.
 *
 * @author yur
 */
public final class DefaultSymbolTable extends BasicSymbolTable {
    public DefaultSymbolTable(ErrorListener errorListener) {
        super(null, errorListener);

        populate();
    }

    private void putDefaultType(String name, MiodType type) {
        SymbolDesc desc = new SymbolDesc(name, null, type, SymbolVisibility.PUBLIC);
        put(new TypeDefSymbol(desc));
    }

    private void populate() {
        // TODO define symbols for 'int16', 'nstring' etc.
        putDefaultType("int", IntegerType.INT32);
        putDefaultType("int8", IntegerType.INT8);
        putDefaultType("int16", IntegerType.INT16);
        putDefaultType("int32", IntegerType.INT32);
        putDefaultType("cardinal", IntegerType.CARDINAL);

        // TODO define built-in annotations
    }

}
