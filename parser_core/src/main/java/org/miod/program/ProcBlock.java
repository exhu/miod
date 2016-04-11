/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.ArrayList;
import java.util.List;

/** Statements block.
 *
 * @author yur
 */
public final class ProcBlock extends BaseSymbolTable {
    private final List<ProcBlock> finals = new ArrayList<>();
    private final List<ProcBlock> blocks = new ArrayList<>();
    public ProcBlock(SymbolTable parent) {
        super(parent);
    }
}
