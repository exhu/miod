/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.miod.program.types.MiodType;

/**
 *
 * @author yur
 */
public final class ExprNodeType extends ExprNodeData {
    public final MiodType typespec;

    public static ExprNodeType newTypespec(MiodType t) {
        return new ExprNodeType(t);
    }

    private ExprNodeType(MiodType t) {
        this.typespec = t;
    }
}
