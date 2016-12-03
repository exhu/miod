/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.miod.parser.ErrorListener;
import org.miod.program.errors.OperationNotSupported;
import org.miod.program.errors.TypesMismatch;
import org.miod.program.values.BoolValue;
import org.miod.program.values.EqualOp;
import org.miod.program.values.ErrorValue;
import org.miod.program.values.LessThanOp;
import org.miod.program.values.MiodValue;
import org.miod.program.values.RuntimeValue;

/**
 * Evaluates expressions. e.g. boolean, integer operations etc. Used for
 * static_if, for Runtime type checks.
 *
 * @author yur
 */
public final class ExpressionEval {

    public static boolean comparableValues(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left.getType().isComparableTo(right.getType())) {
            return true;
        } else {
            errors.onError(new TypesMismatch());
        }
        return false;
    }

    /*
        null <= 3 = null
        1 <= 3 = BoolValue(FALSE)
        int <= 4 = RuntimeValue(Bool)        
     */
    public static MiodValue exprLessOrEqual(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left == null || right == null) {
            return null;
        }

        if (comparableValues(left, right, errors)) {
            if (left.getType().supportsLessThanOp()) {
                if (left instanceof LessThanOp) {
                    LessThanOp op = (LessThanOp) left;
                    if (op.lessThanOrEqual((LessThanOp) right)) {
                        return BoolValue.TRUE;
                    } else {
                        return BoolValue.FALSE;
                    }
                } else {
                    return RuntimeValue.BOOL;
                }
            } else {
                errors.onError(new OperationNotSupported());
                return ErrorValue.UNSUPPORTED;
            }
        }
        return ErrorValue.TYPES_MISMATCH;
    }

    public static MiodValue exprLess(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left == null || right == null) {
            return null;
        }

        if (comparableValues(left, right, errors)) {
            if (left instanceof LessThanOp) {
                LessThanOp op = (LessThanOp) left;
                if (op.lessThan((LessThanOp) right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            } else {
                errors.onError(new OperationNotSupported());
                return ErrorValue.UNSUPPORTED;
            }
        }
        return ErrorValue.TYPES_MISMATCH;
    }

    public static MiodValue exprEq(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left == null || right == null) {
            return null;
        }

        if (comparableValues(left, right, errors)) {
            if (left instanceof EqualOp) {
                EqualOp op = (EqualOp) left;
                if (op.equal((EqualOp) right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            } else {
                errors.onError(new OperationNotSupported());
                return ErrorValue.UNSUPPORTED;
            }
        }

        return ErrorValue.TYPES_MISMATCH;
    }

    public static MiodValue invertBool(MiodValue v) {
        if (v == null) {
            return null;
        }

        if (v == BoolValue.TRUE) {
            return BoolValue.FALSE;
        } else if (v == BoolValue.FALSE) {
            return BoolValue.TRUE;
        }
        return v;
    }

    private ExpressionEval() {
    }
}
