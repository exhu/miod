/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.miod.parser.ParserContext;
import org.miod.parser.expr.ExprNodeData;
import org.miod.parser.expr.ExpressionEval;
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
import org.miod.program.errors.TypesMismatch;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.symbols.ConstSymbol;
import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;
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
public class SemanticVisitor extends MiodParserBaseVisitor<ExprNodeData> {
    private static final Logger LOGGER = Logger.getLogger(SemanticVisitor.class.getName());

    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;

    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }

    @Override
    public ExprNodeData visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        MiodValue res = visit(ctx.boolExpr()).value;
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
    public ExprNodeData visitUnitHeader(MiodParser.UnitHeaderContext ctx) {
        unit = new CompilationUnit(context.getDefaultSymbolTable(), unitName,
                0, 0, unitName, context.getErrorListener());
        context.putUnit(unitName, unit);
        return super.visitUnitHeader(ctx);
    }

    @Override
    public ExprNodeData visitLiteralNull(MiodParser.LiteralNullContext ctx) {
        return ExprNodeData.newValue(NullValue.VALUE);
    }

    @Override
    public ExprNodeData visitLiteralFalse(MiodParser.LiteralFalseContext ctx) {
        return ExprNodeData.newValue(BoolValue.FALSE);
    }

    @Override
    public ExprNodeData visitLiteralTrue(MiodParser.LiteralTrueContext ctx) {
        return ExprNodeData.newValue(BoolValue.TRUE);
    }

    @Override
    public ExprNodeData visitLiteralInteger(MiodParser.LiteralIntegerContext ctx) {
        return ExprNodeData.newValue(new IntegerValue(Long.parseLong(ctx.INTEGER().getText())));
    }

    @Override
    public ExprNodeData visitExprLiteral(MiodParser.ExprLiteralContext ctx) {
        return visit(ctx.literal());
    }



    @Override
    public ExprNodeData visitExprGreater(MiodParser.ExprGreaterContext ctx) {    ;
        return ExprNodeData.newValue(invertBool(exprLessOrEqual(
                visit(ctx.left).value, visit(ctx.right).value,
                context.getErrorListener())));
    }

    @Override
    public ExprNodeData visitExprLess(MiodParser.ExprLessContext ctx) {
        return ExprNodeData.newValue(exprLess(visit(ctx.left).value,
                visit(ctx.right).value,
                context.getErrorListener()));
    }

    @Override
    public ExprNodeData visitExprGreaterEq(MiodParser.ExprGreaterEqContext ctx) {
        return ExprNodeData.newValue(exprLessOrEqual(visit(ctx.right).value,
                visit(ctx.left).value,
                context.getErrorListener()));
    }

    @Override
    public ExprNodeData visitExprLessEq(MiodParser.ExprLessEqContext ctx) {
        return ExprNodeData.newValue(exprLessOrEqual(visit(ctx.left).value,
                visit(ctx.right).value,
                context.getErrorListener()));
    }

    @Override
    public ExprNodeData visitExprEquals(MiodParser.ExprEqualsContext ctx) {
        return ExprNodeData.newValue(exprEq(visit(ctx.left).value, visit(ctx.right).value,
                context.getErrorListener()));
    }

    @Override
    public ExprNodeData visitExprNotEq(MiodParser.ExprNotEqContext ctx) {
        return ExprNodeData.newValue(invertBool(exprEq(visit(ctx.left).value,
                visit(ctx.right).value,
                context.getErrorListener())));
    }



    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.

    @Override
    public ExprNodeData visitConstExpr(MiodParser.ConstExprContext ctx) {
        return visit(ctx.expr()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExprNodeData visitConstAssign(MiodParser.ConstAssignContext ctx) {
        LOGGER.log(Level.INFO, "const assign var = {0}, expr = {1}", new String[] {
                ctx.bareName().getText(),
                ctx.constExpr().getText()});

        // check if already defined
        String id = ctx.bareName().getText();
        SymbolLocation loc = ExpressionEval.makeSymLocation(unitName, ctx.getStart());
        SymbolTableItem sym = unit.symTable.resolve(id);
        if (sym != null) {
            context.getErrorListener().onError(new SymbolRedefinitionError(sym, loc));
            return null;
        }

        // TODO get visibility from context
        SymbolVisibility visibility = SymbolVisibility.PUBLIC;
        SymbolDesc desc = new SymbolDesc(id, loc, null, visibility);
        
        ExprNodeData nodeValue = visit(ctx.constExpr());
        MiodValue value = nodeValue.value;
        // TODO create symbol, put to table
        if (ctx.typeSpec() != null) {
            // TODO check type of the expr
            LOGGER.log(Level.INFO, "typed const");            
            ExprNodeData typeSpec = visit(ctx.typeSpec());

            MiodType targetType = typeSpec.typespec;

            if (value instanceof IntegerValue && targetType instanceof IntegerType) {
                value = ((IntegerValue)value).convertTo((IntegerType)targetType);
            }
            if (typeSpec.typespec != value.getType()) {
                context.getErrorListener().onError(new TypesMismatch());
                return null;
            }
            desc.type = typeSpec.typespec;
            // TODO handle IntegerValue conversion to lower bits type
        } else {
            // TODO type of the expr
            LOGGER.log(Level.INFO, "auto const");            
            desc.type = nodeValue.value.getType();
        }
        unit.symTable.put(new ConstSymbol(desc, value));
        return null;
    }

    @Override
    public ExprNodeData visitExprQualifName(MiodParser.ExprQualifNameContext ctx) {
        SymbolTableItem sym = unit.symTable.resolve(ctx.qualifName().getText());
        if (sym != null) {
            return ExprNodeData.newFromSymbolTableItem(sym);
        }
        return null;
    }

    @Override
    public ExprNodeData visitTypeSpecName(MiodParser.TypeSpecNameContext ctx) {
        SymbolTableItem sym = unit.symTable.resolve(ctx.qualifName().getText());
        if (sym != null) {
            return ExprNodeData.newFromSymbolTableItem(sym);
        }
        return null;
    }


}
