/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.values;

/**
 *
 * @author yur
 */
public interface LessThanOp {
    BoolValue lessThan(LessThanOp other);
    BoolValue lessThanOrEqual(LessThanOp other);
}
