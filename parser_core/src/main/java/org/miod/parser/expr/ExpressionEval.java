/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.antlr.v4.runtime.Token;
import org.miod.parser.ErrorListener;
import org.miod.program.errors.OperationNotSupported;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.types.MiodType;
import org.miod.program.values.BoolValue;
import org.miod.program.values.EqualOp;
import org.miod.program.values.ErrorValue;
import org.miod.program.values.LessThanOp;
import org.miod.program.values.MiodValue;
import org.miod.program.values.PlusOp;
import org.miod.program.values.RuntimeValue;

/**
 * Evaluates expressions. e.g. boolean, integer operations etc. Used for
 * static_if, for Runtime type checks.
 *
 * @author yur
 */
public final class ExpressionEval {
    public static boolean runtimeValues(MiodValue left, MiodValue right) {
        return (left instanceof RuntimeValue) || (right instanceof RuntimeValue);
    }

    public static boolean nulls(MiodValue left, MiodValue right) {
        return left == null || right == null;
    }

    public static MiodValue apply(MiodValue left,
            MiodValue right, RuntimeValue rtValue, ErrorListener errors,
            ExprApply iface) {
        if (nulls(left, right))
            return null;

        if (iface.supportsOp(left.getType(), right.getType())) {
            if (!runtimeValues(left, right)) {
                return iface.apply(left, right);
            } else {
                return rtValue;
            }
        }
        
        errors.onError(new OperationNotSupported());
        return ErrorValue.UNSUPPORTED;
    }

    /*
        null <= 3 = null
        1 <= 3 = BoolValue(FALSE)
        int <= 4 = RuntimeValue(Bool)        
     */
    public static MiodValue exprLessOrEqual(MiodValue left, MiodValue right,
            ErrorListener errors) {
        return apply(left, right, RuntimeValue.BOOL, errors, new ExprApply() {
            @Override
            public boolean supportsOp(MiodType left, MiodType right) {
                return left.supportsLessThanOp(right);
            }

            @Override
            public MiodValue apply(MiodValue left, MiodValue right) {
                LessThanOp op = (LessThanOp) left;
                if (op.lessThanOrEqual((LessThanOp) right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            }
        });
    }

    public static MiodValue exprLess(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (nulls(left, right)) {
            return null;
        }

        if (left.getType().supportsLessThanOp(right.getType())) {
            if (!runtimeValues(left, right)) {
                LessThanOp op = (LessThanOp) left;
                if (op.lessThan((LessThanOp) right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            } else {
                return RuntimeValue.BOOL;
            }
        }
        errors.onError(new OperationNotSupported());
        return ErrorValue.UNSUPPORTED;
    }

    public static MiodValue exprEq(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left == null || right == null) {
            return null;
        }

        if (left.getType().supportsEqualOp(right.getType())) {
            if (!runtimeValues(left, right)) {
                EqualOp op = (EqualOp) left;
                if (op.equal((EqualOp) right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            } else {
                return RuntimeValue.BOOL;
            }
        }
        errors.onError(new OperationNotSupported());
        return ErrorValue.UNSUPPORTED;
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

    public static SymbolLocation makeSymLocation(String unitName, Token token) {
        return new SymbolLocation(unitName, token.getLine(), token.getCharPositionInLine());
    }

    public static MiodValue exprPlus(MiodValue left, MiodValue right,
            ErrorListener errors) {
        if (left == null || right == null) {
            return null;
        }

        if (left.getType().supportsPlusOp(right.getType())) {
            if (left instanceof PlusOp) {
                PlusOp op = (PlusOp) left;
                return op.plusOp(right);
            } else {
                
            }
        }
        errors.onError(new OperationNotSupported());
        return ErrorValue.UNSUPPORTED;
    }

    private ExpressionEval() {
    }
}
