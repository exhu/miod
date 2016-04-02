/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program;

/**
 *
 * @author yur
 */
public interface SymbolTable {
    /// "::ID" skips first match and goes higher to the parent
    SymItem resolve(String id);
    
    void put(SymItem item);
    SymItem get(String id);
}
