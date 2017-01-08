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
import org.miod.program.types.NumericType;
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
public final class ExprEvalHelpers {
    private ExprEvalHelpers() {
    }

    public static boolean runtimeValues(MiodValue left, MiodValue right) {
        return (left instanceof RuntimeValue) || (right instanceof RuntimeValue);
    }

    public static boolean nulls(Object left, Object right) {
        return left == null || right == null;
    }

    public static MiodValue apply(MiodValue left,
            MiodValue right, ErrorListener errors,
            ExprApply iface, SymbolLocation location) {
        if (nulls(left, right))
            return null;

        if (iface.supportsOp(left.getType(), right.getType())) {
            if (!runtimeValues(left, right)) {
                return iface.apply(left, right);
            } else {
                return iface.runtimeValue(left.getType(), right.getType());
            }
        }
        
        errors.onError(new OperationNotSupported(location));
        return ErrorValue.UNSUPPORTED;
    }

    /*
        null <= 3 = null
        1 <= 3 = BoolValue(FALSE)
        int <= 4 = RuntimeValue(Bool)        
     */
    public static MiodValue exprLessOrEqual(MiodValue left, MiodValue right,
            ErrorListener errors, SymbolLocation location) {
        return apply(left, right, errors, new ExprApply() {
            @Override
            public boolean supportsOp(MiodType left, MiodType right) {
                return left.supportsLessThanOp(right);
            }

            @Override
            public MiodValue apply(MiodValue left, MiodValue right) {
                LessThanOp op = (LessThanOp) left;
                return op.lessThanOrEqual((LessThanOp) right);
            }

            @Override
            public MiodValue runtimeValue(MiodType left, MiodType right) {
                return RuntimeValue.BOOL;
            }
        }, location);
    }

    public static MiodValue exprLess(MiodValue left, MiodValue right,
            ErrorListener errors, SymbolLocation location) {
        return apply(left, right, errors, new ExprApply() {
            @Override
            public boolean supportsOp(MiodType left, MiodType right) {
                return left.supportsLessThanOp(right);
            }

            @Override
            public MiodValue apply(MiodValue left, MiodValue right) {
                LessThanOp op = (LessThanOp) left;
                return op.lessThan((LessThanOp) right);
            }

            @Override
            public MiodValue runtimeValue(MiodType left, MiodType right) {
                return RuntimeValue.BOOL;
            }
        }, location);
    }

    public static MiodValue exprEq(MiodValue left, MiodValue right,
            ErrorListener errors, SymbolLocation location) {
        return apply(left, right, errors, new ExprApply() {
            @Override
            public boolean supportsOp(MiodType left, MiodType right) {
                return left.supportsEqualOp(right);
            }

            @Override
            public MiodValue apply(MiodValue left, MiodValue right) {
                EqualOp op = (EqualOp) left;
                return op.equal((EqualOp) right);
            }

            @Override
            public MiodValue runtimeValue(MiodType left, MiodType right) {
                return RuntimeValue.BOOL;
            }
        }, location);
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
            ErrorListener errors, SymbolLocation location) {
        
        return apply(left, right, errors, new ExprApply() {            
            @Override
            public boolean supportsOp(MiodType left, MiodType right) {
                return left.supportsPlusOp(right);
            }

            @Override
            public MiodValue apply(MiodValue left, MiodValue right) {
                PlusOp op = (PlusOp) left;
                return op.plusOp(right);
            }

            @Override
            public MiodValue runtimeValue(MiodType left, MiodType right) {
                if (left instanceof NumericType && right instanceof NumericType) {
                    return RuntimeValue.fromType(((NumericType)left).promote((NumericType)right));
                }
                return RuntimeValue.fromType(left);
            }
        }, location);
    }

    public static String extractStringFromLiteral(String literalText) {
        // TODO strip quotes
        // TODO translate 'escape sequences'
        return literalText;
    }    
}
