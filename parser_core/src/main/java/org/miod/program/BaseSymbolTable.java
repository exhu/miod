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
public abstract class BaseSymbolTable implements SymbolTable {
    private Map<String, SymItem> items = new HashMap<>();
    protected SymbolTable parent;
    public static final String NAMESPACE_SEP = "::";
    
    BaseSymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public SymItem get(String id) {
        return items.get(id);
    }

    @Override
    public void put(String id, SymItem item) {
        if (get(id) == null) {
            items.put(id, item);
        } else {
            throw new SymbolRedefinitionError(item);
        }
    }

    @Override
    public SymItem resolve(String id) {
        // TODO use parent
        // TODO split by "::" and try to resolve
        if (id.startsWith(NAMESPACE_SEP)) {
            if (parent != null)
                return parent.resolve(id.substring(NAMESPACE_SEP.length()));
        }
        return null;
    }
    
    
    
    
}
