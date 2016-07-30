/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import java.util.HashMap;
import java.util.Map;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolTableItem;

/** DEPRECATED.
 *
 * @author yur
 */
public final class EnumType extends MiodType implements SymbolTable {
    final Map<String, Integer> values = new HashMap<>();

    public EnumType() {
        super(ValueTypeId.ENUM_DEF);
    }

    @Override
    public SymbolTableItem resolve(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void put(SymbolTableItem item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SymbolTableItem get(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
