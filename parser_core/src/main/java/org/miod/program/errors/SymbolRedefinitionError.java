/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program.errors;

import org.miod.program.SymItem;

/**
 *
 * @author yur
 */
public class SymbolRedefinitionError extends RuntimeException {
    public SymbolRedefinitionError(SymItem sym) {
        super(sym.toString());
    }
}
