/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public final class UnitNotFoundError extends CompilerError {
    public UnitNotFoundError(String unitName) {
        super(String.format("Unit '%s' not found.", unitName));
    }
}
