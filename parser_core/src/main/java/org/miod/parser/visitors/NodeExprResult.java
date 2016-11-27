/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public final class NodeExprResult {
    public ExprResultKind kind;
    /// stores evaluation result if possible
    public MiodValue value;
}
