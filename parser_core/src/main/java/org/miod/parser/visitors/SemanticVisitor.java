/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.parser.ParserContext;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.BooleanExprExpected;
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.errors.OperationNotSupported;
import org.miod.program.errors.TypesMismatch;
import org.miod.program.types.TypeUtils;
import org.miod.program.types.ValueTypeId;
import org.miod.program.values.BoolValue;
import org.miod.program.values.EqualOp;
import org.miod.program.values.IntegerValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;
import org.miod.program.values.RuntimeValue;
import org.miod.program.values.LessThanOp;

/**
 * First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 * TODO deprecate ExprNodeData, use MiodValue directly.
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<MiodValue> {
    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;

    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }

    @Override
    public MiodValue visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        MiodValue res = visit(ctx.boolExpr());
        if (res == null || res instanceof RuntimeValue) {
            context.getErrorListener().onError(new CompileTimeExpressionExpected());
        } else {
            if (res.getType().typeId == ValueTypeId.BOOL) {
                if (((BoolValue)res).value == true) {
                    return visit(ctx.trueStmts);
                } else {
                    return visit(ctx.falseStmts);
                }
            } else {
                context.getErrorListener().onError(new BooleanExprExpected());
            }
        }
        return null;
    }

    @Override
    public MiodValue visitUnitHeader(MiodParser.UnitHeaderContext ctx) {
        unit = new CompilationUnit(context.getDefaultSymbolTable(), unitName,
                0, 0, unitName, context.getErrorListener());        
        context.putUnit(unitName, unit);
        return super.visitUnitHeader(ctx);
    }

    @Override
    public MiodValue visitLiteralNull(MiodParser.LiteralNullContext ctx) {
        return NullValue.VALUE;
    }

    @Override
    public MiodValue visitLiteralFalse(MiodParser.LiteralFalseContext ctx) {
        return BoolValue.FALSE;
    }

    @Override
    public MiodValue visitLiteralTrue(MiodParser.LiteralTrueContext ctx) {
        return BoolValue.TRUE;
    }

    @Override
    public MiodValue visitLiteralInteger(MiodParser.LiteralIntegerContext ctx) {
        return new IntegerValue(Long.parseLong(ctx.INTEGER().getText()));
    }

    @Override
    public MiodValue visitExprLiteral(MiodParser.ExprLiteralContext ctx) {
        return visit(ctx.literal());
    }

    private boolean comparableValues(MiodValue left, MiodValue right) {
        if (left != null && right != null && !(left instanceof RuntimeValue)
                && !(right instanceof RuntimeValue)) {
            if (TypeUtils.isComparable(left.getType().typeId, right.getType().typeId)) {
                return true;
            } else {
                context.getErrorListener().onError(new TypesMismatch());
            }
        }
        return false;
    }

    private MiodValue exprLessOrEqual(MiodValue left, MiodValue right) {
        if (comparableValues(left, right)) {
            if(left instanceof LessThanOp) {
                LessThanOp op = (LessThanOp)left;
                if (op.lessThanOrEqual((LessThanOp)right))
                    return BoolValue.TRUE;
                else
                    return BoolValue.FALSE;
            } else {
                context.getErrorListener().onError(new OperationNotSupported());
            }
        }
        return null;
    }

    private MiodValue exprLess(MiodValue left, MiodValue right) {
        if (comparableValues(left, right)) {
            if(left instanceof LessThanOp) {
                LessThanOp op = (LessThanOp)left;
                if (op.lessThan((LessThanOp)right))
                    return BoolValue.TRUE;
                else
                    return BoolValue.FALSE;
            } else {
                context.getErrorListener().onError(new OperationNotSupported());
            }
        }
        return null;
    }

    private MiodValue exprEq(MiodValue left, MiodValue right) {
        if (comparableValues(left, right)) {
            if (left instanceof EqualOp) {
                EqualOp op = (EqualOp)left;
                if (op.equal((EqualOp)right)) {
                    return BoolValue.TRUE;
                } else {
                    return BoolValue.FALSE;
                }
            } else {
                context.getErrorListener().onError(new OperationNotSupported());
            }
        }

        return null;
    }

    private static MiodValue invertBool(MiodValue v) {
        if (v == BoolValue.TRUE)
            return BoolValue.FALSE;
        else if (v == BoolValue.FALSE)
            return BoolValue.TRUE;
        return v;
    }

    @Override
    public MiodValue visitExprGreater(MiodParser.ExprGreaterContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        return invertBool(exprLessOrEqual(left, right));
    }

    @Override
    public MiodValue visitExprLess(MiodParser.ExprLessContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        return exprLess(left, right);
    }

    @Override
    public MiodValue visitExprGreaterEq(MiodParser.ExprGreaterEqContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        return exprLessOrEqual(right, left);
    }

    @Override
    public MiodValue visitExprLessEq(MiodParser.ExprLessEqContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        return exprLessOrEqual(left, right);
    }

    @Override
    public MiodValue visitExprEquals(MiodParser.ExprEqualsContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);        
        return exprEq(left, right);
    }

    @Override
    public MiodValue visitExprNotEq(MiodParser.ExprNotEqContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        return invertBool(exprEq(left, right));
    }

    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.
}
