/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program.types;

import org.miod.program.symbol_table.SymbolType;
import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public class TypeSystem {

    boolean isCompatible(SymbolType a, SymbolType b) {
        return false;
    }

    MiodValue valueFromString(String literal) {
        return null;
    }
}
