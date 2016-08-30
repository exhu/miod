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
    /// Can be null for the first pass, e.g. yet unknown identifiers,
    /// error if null for the second pass.
    /// Contains explicit value for literals and through constants expansion,
    /// RuntimeValue for vars, function calls, array indexing etc.
    public MiodValue value;

    public ExprNodeData(MiodValue v) {
        value = v;
    }
}
