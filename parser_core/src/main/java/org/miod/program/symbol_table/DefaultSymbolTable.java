/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import org.miod.parser.ErrorListener;

/** Defines language builtin types.
 *
 * @author yur
 */
public final class DefaultSymbolTable extends BasicSymbolTable {
    public DefaultSymbolTable(ErrorListener errorListener) {
        super(null, errorListener);

        populate();
    }

    private void populate() {
        // TODO define symbols for 'int16', 'nstring' etc.
    }

}