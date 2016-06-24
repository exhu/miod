/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.miod.parser.ErrorListener;

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
    public final String parentNamespace;
    
    
    public GlobalSymbolTable(String unitName, ErrorListener errorListener) {
        super(null, errorListener);
        this.unitName = unitName;
        this.parentNamespace = getParentNamespace(unitName);
    }
    
    final public static String getParentNamespace(String unitName) {        
        final int subIndex = unitName.lastIndexOf(NAMESPACE_SEP);
        if (subIndex > 0)
            return unitName.substring(0, subIndex);
        return "";
    }
    
    /// adds import into resolution paths, not into symbol table!
    final public void addImport(GlobalSymbolTable item, boolean fullNamesOnly) {
        imports.add(new Imported(item, fullNamesOnly));
    }
    
    @Override
    final public SymItem resolve(String id) {
        SymItem item = resolveImmediateOnly(id);
        if (item == null) {
            return resolveFromImports(id);
        }
        return item;
    }
    
    /// Does not look into imports
    public final SymItem resolveImmediateOnly(String id) {
        SymItem item = super.resolve(id);
        if (item == null) {
            if (id.startsWith(unitName)) {
                // look no further
                return get(id.substring(unitName.length() + NAMESPACE_SEP.length()));
            }            
        }
        return item;
    }
    
    private SymItem resolveFromImports(String id) {
        // resolve from last import to first
        // ignore "private" symbols
        // check for same package and ignore "protected"
        ListIterator<Imported> i = imports.listIterator(imports.size());
        while(i.hasPrevious()) {
            Imported item = i.previous();
            if (item.fullNamesOnly == false || item.fullNamesOnly && id.startsWith(item.table.unitName)) {
                SymItem resolved = item.table.resolveImmediateOnly(id);
                if (resolved != null && 
                        (resolved.visibility == SymVisibility.Public ||
                        (resolved.visibility == SymVisibility.Protected && 
                        item.table.parentNamespace.equals(parentNamespace))))
                    return resolved;
            }
        }
        return null;
    }
}
