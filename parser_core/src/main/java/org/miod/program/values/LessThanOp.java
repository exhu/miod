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
    boolean lessThan(LessThanOp other);
    boolean lessThanOrEqual(LessThanOp other);
}
