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
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.values.NullValue;

/**
 * First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<ExprNodeData> {

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
        if (res == null) {
            context.getErrorListener().onError(new CompileTimeExpressionExpected());
        } else {
            // TODO if true, visit(ctx.trueStmts), else ctx.falseStmts
        }
        return res;
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
        return new ExprNodeData(NullValue.value);
    }


}
