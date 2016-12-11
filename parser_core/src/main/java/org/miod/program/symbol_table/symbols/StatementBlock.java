/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table.symbols;

import org.miod.parser.ErrorListener;
import org.miod.program.symbol_table.BasicSymbolTable;
import org.miod.program.symbol_table.SymbolTable;

/** Describes a code block.
 *
 * @author yur
 */
public final class StatementBlock {
    // multiple finally blocks are merged into one on parsing.
    public StatementBlock finallyBlock;
    public final BasicSymbolTable symTable;

    public StatementBlock(SymbolTable parent, ErrorListener errorListener) {
        symTable = new BasicSymbolTable(parent, errorListener);
    }

}
