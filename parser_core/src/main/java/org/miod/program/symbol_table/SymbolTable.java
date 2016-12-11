/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

/**
 *
 * @author yur
 */
public interface SymbolTable {
    SymbolTable getParentTable();

    /// "::ID" skips first match and goes higher to the parent
    SymbolTableItem resolve(String id);
    
    void put(SymbolTableItem item);
    SymbolTableItem get(String id);

    public static final String NAMESPACE_SEP = "::";

    public static String nameFromFullName(String full) {
        String name;
        int index = full.lastIndexOf(NAMESPACE_SEP);
        if (index < 0) {
            return full;
        }
        return full.substring(index + NAMESPACE_SEP.length());
    }
}
