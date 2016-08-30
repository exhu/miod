/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.parser.expr;

import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public final class ExprNodeData {
    /// can be null for the first pass, e.g. yet unknown identifiers
    /// explicit value for literals and through constants expansion,
    /// RuntimeValue for vars and function calls
    public MiodValue value;

    public ExprNodeData(MiodValue v) {
        value = v;
    }
}
