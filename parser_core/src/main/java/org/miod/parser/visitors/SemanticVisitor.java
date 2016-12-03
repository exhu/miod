/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.miod.parser.ParserContext;
import static org.miod.parser.expr.ExpressionEval.exprEq;
import static org.miod.parser.expr.ExpressionEval.exprLess;
import static org.miod.parser.expr.ExpressionEval.exprLessOrEqual;
import static org.miod.parser.expr.ExpressionEval.invertBool;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.BooleanExprExpected;
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.errors.SymbolRedefinitionError;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.types.ValueTypeId;
import org.miod.program.values.BoolValue;
import org.miod.program.values.IntegerValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;
import org.miod.program.values.RuntimeValue;

/**
 * First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions. TODO deprecate ExprNodeData, use MiodValue directly.
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<MiodValue> {
    private static final Logger LOGGER = Logger.getLogger(SemanticVisitor.class.getName());

    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;

    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }

    /// signal error "expected expression" for second pass if v == null or ErrorValue
    protected MiodValue checkValue(MiodValue v) {
        return v;
    }

    @Override
    public MiodValue visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        MiodValue res = visit(ctx.boolExpr());
        if (res == null || res instanceof RuntimeValue) {
            context.getErrorListener().onError(new CompileTimeExpressionExpected());
        } else if (res.getType().typeId == ValueTypeId.BOOL) {
            if (((BoolValue) res).value == true) {
                return visit(ctx.trueStmts);
            } else {
                return visit(ctx.falseStmts);
            }
        } else {
            context.getErrorListener().onError(new BooleanExprExpected());
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



    @Override
    public MiodValue visitExprGreater(MiodParser.ExprGreaterContext ctx) {    ;
        return checkValue(invertBool(exprLessOrEqual(visit(ctx.left), visit(ctx.right),
                context.getErrorListener())));
    }

    @Override
    public MiodValue visitExprLess(MiodParser.ExprLessContext ctx) {        
        return checkValue(exprLess(visit(ctx.left), visit(ctx.right),
                context.getErrorListener()));
    }

    @Override
    public MiodValue visitExprGreaterEq(MiodParser.ExprGreaterEqContext ctx) {        
        return checkValue(exprLessOrEqual(visit(ctx.right), visit(ctx.left),
                context.getErrorListener()));
    }

    @Override
    public MiodValue visitExprLessEq(MiodParser.ExprLessEqContext ctx) {        
        return checkValue(exprLessOrEqual(visit(ctx.left), visit(ctx.right),
                context.getErrorListener()));
    }

    @Override
    public MiodValue visitExprEquals(MiodParser.ExprEqualsContext ctx) {        
        return checkValue(exprEq(visit(ctx.left), visit(ctx.right),
                context.getErrorListener()));
    }

    @Override
    public MiodValue visitExprNotEq(MiodParser.ExprNotEqContext ctx) {        
        return checkValue(invertBool(exprEq(visit(ctx.left), visit(ctx.right),
                context.getErrorListener())));
    }



    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.

    @Override
    public MiodValue visitConstExpr(MiodParser.ConstExprContext ctx) {
        return visit(ctx.expr()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MiodValue visitConstAssign(MiodParser.ConstAssignContext ctx) {
        LOGGER.log(Level.INFO, "const assign var = {0}, expr = {1}", new String[] {
                ctx.bareName().getText(),
                ctx.constExpr().getText()});

        // check if already defined
        String id = ctx.bareName().getText();
        SymbolTableItem sym = unit.symTable.resolve(id);
        if (sym != null) {
            context.getErrorListener().onError(new SymbolRedefinitionError(sym));
            return null;
        }

        // TODO create symbol, put to table

        if (ctx.typeSpec() != null) {
            // TODO check type of the expr
            LOGGER.log(Level.INFO, "typed const");
        } else {
            // TODO type of the expr
            LOGGER.log(Level.INFO, "auto const");
        }
        return super.visitConstAssign(ctx);
    }


}
