/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.parser.ParserContext;
import org.miod.parser.expr.ExprNodeData;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.BooleanExprExpected;
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.errors.OperationNotSupported;
import org.miod.program.errors.TypesMismatch;
import org.miod.program.types.PrimitiveType;
import org.miod.program.types.ValueTypeId;
import org.miod.program.values.BoolValue;
import org.miod.program.values.IntegerValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;
import org.miod.program.values.RuntimeValue;

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

    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.

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

    @Override
    public MiodValue visitExprGreater(MiodParser.ExprGreaterContext ctx) {
        MiodValue left = visit(ctx.left);
        MiodValue right = visit(ctx.right);
        if (left != null && right != null && !(left instanceof RuntimeValue)
                && !(right instanceof RuntimeValue)) {
            // TODO boolean values
            if (PrimitiveType.compatible(left.getType().typeId, right.getType().typeId)) {
                if (left instanceof IntegerValue) {
                    IntegerValue a = (IntegerValue)left;
                    IntegerValue b = (IntegerValue)right;
                    if (a.value > b.value)
                        return BoolValue.TRUE;
                    return BoolValue.FALSE;
                } else {
                    context.getErrorListener().onError(new OperationNotSupported());
                }
            } else {
                context.getErrorListener().onError(new TypesMismatch());
            }
        }
        return null;
    }

    

}
