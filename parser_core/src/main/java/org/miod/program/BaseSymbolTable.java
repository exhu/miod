/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

import java.util.HashMap;
import java.util.Map;
import org.miod.program.errors.SymbolRedefinitionError;

/**
 *
 * @author yur
 */
public class BaseSymbolTable implements SymbolTable {
    final private Map<String, SymItem> items = new HashMap<>();
    final protected SymbolTable parent;
    public static final String NAMESPACE_SEP = "::";
    
    BaseSymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    final public SymItem get(String id) {
        return items.get(id);
    }

    @Override
    final public void put(String id, SymItem item) {
        if (get(id) == null) {
            items.put(id, item);
        } else {
            throw new SymbolRedefinitionError(item);
        }
    }
    
    final protected SymItem resolveAlias(SymItem item) {
        if (item.type.isAliasedType())
            return ((AliasedType)item.type).getFinalSymbol();
        return item;
    }

    @Override
    public SymItem resolve(String id) {
        // initial "::" starts global search first
        if (id.startsWith(NAMESPACE_SEP)) {
            id = id.substring(NAMESPACE_SEP.length());
            if (parent != null)
                return parent.resolve(id);
        }
        
        // try best match first
        SymItem item = items.get(id);
        if (item == null) {
            // split by "::" and try to resolve
            int sub = id.indexOf(NAMESPACE_SEP);
            if (sub > 0) {
                SymItem subItem = get(id.substring(0, sub));
                if (subItem != null) {
                    subItem = resolveAlias(subItem);
                    if (subItem.type.isSymbolTable()) {
                        SymbolWithSymTable subTable = (SymbolWithSymTable)subItem.type;
                        item = subTable.resolve(id.substring(sub+NAMESPACE_SEP.length()));
                    }
                }
            }
        }
                
        if (item == null && parent != null) {
            return parent.resolve(id);
        }
        return item;
    }
    
    
    
    
}
