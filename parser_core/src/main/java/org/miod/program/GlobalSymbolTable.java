/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.HashMap;
import java.util.Map;

/** Global program symbol table: predefined consts, System unit, this unit
 *
 * @author yur
 */
public final class GlobalSymbolTable extends BaseSymbolTable {

    public GlobalSymbolTable() {
        super(null);
    }
}
