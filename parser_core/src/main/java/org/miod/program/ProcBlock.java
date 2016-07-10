/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import org.miod.program.symbol_table.BasicSymbolTable;
import org.miod.program.symbol_table.SymbolTable;
import java.util.ArrayList;
import java.util.List;
import org.miod.parser.ErrorListener;

/** Statements block to store variables.
 *
 * @author yur
 */
public final class ProcBlock extends BasicSymbolTable {
    private final List<ProcBlock> finals = new ArrayList<>();
    private final List<ProcBlock> blocks = new ArrayList<>();
    public ProcBlock(SymbolTable parent, ErrorListener errorListener) {
        super(parent, errorListener);
    }
}
