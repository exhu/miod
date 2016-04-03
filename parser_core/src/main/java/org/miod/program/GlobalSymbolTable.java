/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/** Global program symbol table: predefined consts, System unit, this unit
 *
 * @author yur
 */
public final class GlobalSymbolTable extends BaseSymbolTable {
    static private class Imported {
        public final boolean fullNamesOnly;
        public final GlobalSymbolTable table;
        public Imported(GlobalSymbolTable table, boolean fullNamesOnly) {
            this.fullNamesOnly = fullNamesOnly;
            this.table = table;
        }
    }
    private List<Imported> imports = new ArrayList<>();
    public final String unitName;
    
    
    public GlobalSymbolTable(String unitName) {
        super(null);
        this.unitName = unitName;
    }
    
    public void addImport(GlobalSymbolTable item, boolean fullNamesOnly) {
        imports.add(new Imported(item, fullNamesOnly));
    }
    
    @Override
    public SymItem resolve(String id) {
        SymItem item = super.resolve(id);
        if (item == null) {
            if (id.startsWith(unitName)) {
                // look no further
                return get(id.substring(unitName.length() + NAMESPACE_SEP.length()));
            }            
            return resolveFromImports(id);
        }
        return item;
    }
    
    private SymItem resolveFromImports(String id) {
        // resolve from last import to first
        ListIterator<Imported> i = imports.listIterator(imports.size());
        while(i.hasPrevious()) {
            Imported item = i.previous();
            if (item.fullNamesOnly == false || item.fullNamesOnly && id.startsWith(item.table.unitName)) {                     SymItem resolved = item.table.resolve(id);
                if (resolved != null)
                    return resolved;
            }
        }
        return null;
    }
}
