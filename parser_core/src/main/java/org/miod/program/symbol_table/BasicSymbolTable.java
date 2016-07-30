/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.symbol_table;

import java.util.HashMap;
import java.util.Map;
import org.miod.parser.ErrorListener;
import org.miod.program.errors.SymbolRedefinitionError;

/**
 *
 * @author yur
 */
public class BasicSymbolTable implements SymbolTable {
    final private Map<String, SymbolTableItem> items = new HashMap<>();
    final protected SymbolTable parent;
    protected ErrorListener errorListener;

    public BasicSymbolTable(SymbolTable parent, ErrorListener errorListener) {
        this.parent = parent;
        this.errorListener = errorListener;
    }

    @Override
    final public SymbolTableItem get(String id) {
        return items.get(id);
    }

    @Override
    final public void put(SymbolTableItem item) {
        if (get(item.desc.name) == null) {
            items.put(item.desc.name, item);
        } else {
            errorListener.onError(new SymbolRedefinitionError(item));
        }
    }

    @Override
    public SymbolTableItem resolve(String id) {
        // initial "::" starts global search first
        if (id.startsWith(NAMESPACE_SEP)) {
            id = id.substring(NAMESPACE_SEP.length());
            if (parent != null)
                return parent.resolve(id);
        }
        
        // try best match first
        SymbolTableItem item = items.get(id);
        if (item == null) {
            // split by "::" and try to resolve
            int sub = id.indexOf(NAMESPACE_SEP);
            if (sub > 0) {
                SymbolTableItem subItem = get(id.substring(0, sub));
                if (subItem != null) {
                    SymbolWithSymTable symTabledItem = (SymbolWithSymTable)subItem.resolveAlias();
                    if (symTabledItem != null) {
                        item = symTabledItem.resolve(id.substring(sub+NAMESPACE_SEP.length()));
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
