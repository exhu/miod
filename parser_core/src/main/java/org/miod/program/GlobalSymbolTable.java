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
    private List<SymItem> imports = new ArrayList<>();
    private final String unitName;
    
    public GlobalSymbolTable(String unitName) {
        super(null);
        this.unitName = unitName;
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
        ListIterator<SymItem> i = imports.listIterator(imports.size());
        while(i.hasPrevious()) {
            SymItem imported = i.previous();
            SymItem resolved = ((SymbolWithSymTable)(imported.type)).resolve(id);
            if (resolved != null)
                return resolved;
        }
        return null;
    }
}
