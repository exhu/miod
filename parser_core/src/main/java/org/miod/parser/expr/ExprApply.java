/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.miod.program.types.MiodType;
import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public interface ExprApply {
    boolean supportsOp(MiodType left, MiodType right);
    MiodValue apply(MiodValue left, MiodValue right);
}
