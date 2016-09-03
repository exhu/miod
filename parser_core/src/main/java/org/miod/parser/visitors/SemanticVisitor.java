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
import org.miod.program.types.ValueTypeId;
import org.miod.program.values.BoolValue;
import org.miod.program.values.IntegerValue;
import org.miod.program.values.NullValue;
import org.miod.program.values.RuntimeValue;

/**
 * First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 * TODO deprecate ExprNodeData, use MiodValue directly.
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<ExprNodeData> {
    protected final static ExprNodeData NULL_VALUE = new ExprNodeData(NullValue.value);
    protected final static ExprNodeData TRUE_VALUE = new ExprNodeData(BoolValue.TRUE);
    protected final static ExprNodeData FALSE_VALUE = new ExprNodeData(BoolValue.FALSE);

    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;

    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }

    @Override
    public ExprNodeData visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        ExprNodeData res = visit(ctx.boolExpr());
        if (res == null || res.value == null || res.value instanceof RuntimeValue) {
            context.getErrorListener().onError(new CompileTimeExpressionExpected());
        } else {
            if (res.value.getType().typeId == ValueTypeId.BOOL) {
                if (((BoolValue)res.value).value == true) {
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
    public ExprNodeData visitUnitHeader(MiodParser.UnitHeaderContext ctx) {
        unit = new CompilationUnit(context.getDefaultSymbolTable(), unitName,
                0, 0, unitName, context.getErrorListener());        
        context.putUnit(unitName, unit);
        return super.visitUnitHeader(ctx);
    }

    @Override
    public ExprNodeData visitLiteralNull(MiodParser.LiteralNullContext ctx) {
        return NULL_VALUE;
    }

    @Override
    public ExprNodeData visitLiteralFalse(MiodParser.LiteralFalseContext ctx) {
        return FALSE_VALUE;
    }

    @Override
    public ExprNodeData visitLiteralTrue(MiodParser.LiteralTrueContext ctx) {
        return TRUE_VALUE;
    }

    @Override
    public ExprNodeData visitLiteralInteger(MiodParser.LiteralIntegerContext ctx) {
        return new ExprNodeData(new IntegerValue(Long.parseLong(ctx.INTEGER().getText())));
    }

    @Override
    public ExprNodeData visitExprLiteral(MiodParser.ExprLiteralContext ctx) {
        return visit(ctx.literal());
    }

    @Override
    public ExprNodeData visitExprGreater(MiodParser.ExprGreaterContext ctx) {
        ExprNodeData left = visit(ctx.left);
        ExprNodeData right = visit(ctx.right);
        if (left.value.getType() == right.value.getType()) {
            if (left.value instanceof IntegerValue) {
                IntegerValue a = (IntegerValue)left.value;
                IntegerValue b = (IntegerValue)right.value;
                if (a.value > b.value)
                    return TRUE_VALUE;
                return FALSE_VALUE;
            } else {
                context.getErrorListener().onError(new OperationNotSupported());
            } 
        } else {
            context.getErrorListener().onError(new TypesMismatch());
        }
        return null;
    }

    

}
