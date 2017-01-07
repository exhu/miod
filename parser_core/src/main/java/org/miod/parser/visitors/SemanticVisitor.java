/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.Token;
import org.miod.parser.ParserContext;
import org.miod.parser.expr.ExprNodeData;
import org.miod.parser.expr.ExprNodeList;
import org.miod.parser.expr.ExprNodeType;
import org.miod.parser.expr.ExprNodeValue;
import org.miod.parser.expr.ExpressionEval;
import static org.miod.parser.expr.ExpressionEval.exprEq;
import static org.miod.parser.expr.ExpressionEval.exprLess;
import static org.miod.parser.expr.ExpressionEval.exprLessOrEqual;
import static org.miod.parser.expr.ExpressionEval.exprPlus;
import static org.miod.parser.expr.ExpressionEval.invertBool;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.BooleanExprExpected;
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.errors.IntegerExpected;
import org.miod.program.errors.IntegerInBoundsExpected;
import org.miod.program.errors.SymbolRedefinitionError;
import org.miod.program.errors.TypeNameExpected;
import org.miod.program.errors.TypesMismatch;
import org.miod.program.errors.ValueExpected;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.symbol_table.SymbolTable;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.symbols.ConstSymbol;
import org.miod.program.symbol_table.symbols.TypeDefSymbol;
import org.miod.program.types.ArrayRefType;
import org.miod.program.types.ArrayType;
import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;
import org.miod.program.types.ValueTypeId;
import org.miod.program.values.ArrayValue;
import org.miod.program.values.BoolValue;
import org.miod.program.values.IntegerValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;
import org.miod.program.values.RuntimeValue;
import org.miod.program.values.StringValue;

/**
 * First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<ExprNodeData> {

    private static final Logger LOGGER = Logger.getLogger(SemanticVisitor.class.getName());

    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;
    protected SymbolTable currentSymTable;
    public boolean containsUnresolvedSyms = false;

    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }

    @Override
    public ExprNodeData visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        MiodValue res = ((ExprNodeValue)visit(ctx.boolExpr())).value;
        if (res == null || res instanceof RuntimeValue) {
            context.getErrorListener().onError(
                    new CompileTimeExpressionExpected(makeSymLocation(ctx.boolExpr().getStart())));
        } else if (res.getType().typeId == ValueTypeId.BOOL) {
            if (((BoolValue) res).value == true) {
                return visit(ctx.trueStmts);
            } else {
                return visit(ctx.falseStmts);
            }
        } else {
            context.getErrorListener().onError(new BooleanExprExpected(
                    makeSymLocation(ctx.boolExpr().getStart())));
        }
        return null;
    }

    @Override
    public ExprNodeData visitUnitHeader(MiodParser.UnitHeaderContext ctx) {
        unit = new CompilationUnit(context.getDefaultSymbolTable(), unitName,
                0, 0, unitName, context.getErrorListener());
        context.putUnit(unitName, unit);
        currentSymTable = unit.symTable;
        return super.visitUnitHeader(ctx);
    }

    @Override
    public ExprNodeData visitLiteralNull(MiodParser.LiteralNullContext ctx) {
        return ExprNodeValue.newValue(NullValue.VALUE);
    }

    @Override
    public ExprNodeData visitLiteralFalse(MiodParser.LiteralFalseContext ctx) {
        return ExprNodeValue.newValue(BoolValue.FALSE);
    }

    @Override
    public ExprNodeData visitLiteralTrue(MiodParser.LiteralTrueContext ctx) {
        return ExprNodeValue.newValue(BoolValue.TRUE);
    }

    @Override
    public ExprNodeData visitLiteralInteger(MiodParser.LiteralIntegerContext ctx) {
        return ExprNodeValue.newValue(new IntegerValue(Long.parseLong(ctx.INTEGER().getText())));
    }

    @Override
    public ExprNodeData visitLiteralCharStr(MiodParser.LiteralCharStrContext ctx) {
        // TODO strip quotes, return uint8
        return super.visitLiteralCharStr(ctx);
    }

    @Override
    public ExprNodeData visitLiteralString(MiodParser.LiteralStringContext ctx) {        
        return ExprNodeValue.newValue(new StringValue(
                ExpressionEval.extractStringFromLiteral(ctx.STRING().getText())));
    }

    @Override
    public ExprNodeData visitExprLiteral(MiodParser.ExprLiteralContext ctx) {
        return visit(ctx.literal());
    }

    @Override
    public ExprNodeData visitExprGreater(MiodParser.ExprGreaterContext ctx) {
        return ExprNodeValue.newValue(invertBool(exprLessOrEqual(
                ((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart()))));
    }

    @Override
    public ExprNodeData visitExprLess(MiodParser.ExprLessContext ctx) {
        return ExprNodeValue.newValue(exprLess(
                ((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public ExprNodeData visitExprGreaterEq(MiodParser.ExprGreaterEqContext ctx) {
        return ExprNodeValue.newValue(exprLessOrEqual(
                ((ExprNodeValue)visit(ctx.right)).value,
                ((ExprNodeValue)visit(ctx.left)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public ExprNodeData visitExprLessEq(MiodParser.ExprLessEqContext ctx) {
        return ExprNodeValue.newValue(exprLessOrEqual(
                ((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public ExprNodeData visitExprEquals(MiodParser.ExprEqualsContext ctx) {
        return ExprNodeValue.newValue(
                exprEq(((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public ExprNodeData visitExprNotEq(MiodParser.ExprNotEqContext ctx) {
        return ExprNodeValue.newValue(invertBool(exprEq(
                ((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart()))));
    }

    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.
    @Override
    public ExprNodeData visitConstExpr(MiodParser.ConstExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ExprNodeData visitConstAssign(MiodParser.ConstAssignContext ctx) {
        LOGGER.log(Level.INFO, "const assign var = {0}, expr = {1}", new String[]{
            ctx.bareName().getText(),
            ctx.constExpr().getText()});

        // check if already defined
        String id = ctx.bareName().getText();
        SymbolLocation loc = makeSymLocation(ctx.getStart());
        SymbolTableItem sym = currentSymTable.resolve(id);
        if (sym != null) {
            context.getErrorListener().onError(new SymbolRedefinitionError(sym, loc));
            return null;
        }

        // TODO get visibility from context
        SymbolVisibility visibility = SymbolVisibility.PUBLIC;
        SymbolDesc desc = new SymbolDesc(id, loc, null, visibility);

        ExprNodeValue nodeValue = (ExprNodeValue)visit(ctx.constExpr());
        MiodValue value = nodeValue.value;
        // create symbol, put to table
        if (ctx.typeSpec() != null) {
            // check type of the expr
            LOGGER.log(Level.INFO, "typed const");
            ExprNodeType typeSpec = (ExprNodeType)visit(ctx.typeSpec());

            MiodType targetType = typeSpec.typespec;

            if (value instanceof IntegerValue && targetType instanceof IntegerType) {
                value = ((IntegerValue) value).convertTo((IntegerType) targetType);
            }
            if (typeSpec.typespec != value.getType()) {
                context.getErrorListener().onError(new TypesMismatch(makeSymLocation(ctx.typeSpec().getStart())));
                return null;
            }
            desc.type = typeSpec.typespec;
            // TODO handle IntegerValue conversion to lower bits type
        } else {
            // TODO type of the expr
            LOGGER.log(Level.INFO, "auto const");
            desc.type = nodeValue.value.getType();
        }
        currentSymTable.put(new ConstSymbol(desc, value));
        return null;
    }

    protected final ExprNodeData resolveSymbol(String name, SymbolLocation loc) {
        // current context local symbol table
        SymbolTableItem sym = currentSymTable.resolve(name);
        if (sym != null) {
            return ExprNodeData.newFromSymbolTableItem(sym);
        }
        containsUnresolvedSyms = true;
        context.getErrorListener().onUnknownIdentifier(this, name, loc);
        return null;
    }

    @Override
    public ExprNodeData visitQualifName(MiodParser.QualifNameContext ctx) {
        return resolveSymbol(ctx.getText(), makeSymLocation(ctx.getStart()));
    }

    @Override
    public ExprNodeData visitExprQualifName(MiodParser.ExprQualifNameContext ctx) {
        return visit(ctx.qualifName());
    }

    @Override
    public ExprNodeData visitTypeSpecName(MiodParser.TypeSpecNameContext ctx) {
        return resolveSymbol(ctx.qualifName().getText(), makeSymLocation(ctx.qualifName().getStart()));
    }

    @Override
    public ExprNodeData visitExprParen(MiodParser.ExprParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ExprNodeData visitExprPlus(MiodParser.ExprPlusContext ctx) {
        return ExprNodeValue.newValue(exprPlus(
                ((ExprNodeValue)visit(ctx.left)).value,
                ((ExprNodeValue)visit(ctx.right)).value,
                context.getErrorListener(),
                makeSymLocation(ctx.getStart())));
    }

    @Override
    public ExprNodeData visitTypeSpecArray(MiodParser.TypeSpecArrayContext ctx) {
        return visit(ctx.arrayType());
    }

    @Override
    public ExprNodeData visitArrayType(MiodParser.ArrayTypeContext ctx) {
        return visit(ctx.arrayVariant());
    }

    @Override
    public ExprNodeData visitUnknownSizeArray(MiodParser.UnknownSizeArrayContext ctx) {
        ExprNodeData elementType = visit(ctx.qualifName());
        if (elementType == null) {
            return null;
        }

        // expect type name
        if (elementType instanceof ExprNodeType == false) {
            context.getErrorListener().onError(new TypeNameExpected(
                    makeSymLocation(ctx.qualifName().getStart())));
            return null;
        }

        return ExprNodeType.newTypespec(ArrayRefType.fromMiodType(((ExprNodeType)elementType).typespec));
    }

    @Override
    public ExprNodeData visitSizedArray(MiodParser.SizedArrayContext ctx) {
        ExprNodeData elementType = visit(ctx.typeSpec());
        if (elementType == null) {
            return null;
        }

        // expect type name
        if (elementType instanceof ExprNodeType == false) {
            context.getErrorListener().onError(new TypeNameExpected(
                    makeSymLocation(ctx.typeSpec().getStart())));
            return null;
        }

        ExprNodeValue sizeSpec = (ExprNodeValue)visit(ctx.expr());
        if (sizeSpec == null) {
            return null;
        }

        if (sizeSpec.value == null || !(sizeSpec.value instanceof IntegerValue)) {
            context.getErrorListener().onError(new IntegerExpected(
                    makeSymLocation(ctx.expr().getStart())));
            return null;
        }

        long sizeValue = ((IntegerValue) sizeSpec.value).value;

        if (sizeValue <= 0 || sizeValue > Integer.MAX_VALUE) {
            context.getErrorListener().onError(new IntegerInBoundsExpected(
                    makeSymLocation(ctx.expr().getStart()),
                    0, Integer.MAX_VALUE));
            return null;
        }

        return ExprNodeType.newTypespec(new ArrayType((int) ((IntegerValue) sizeSpec.value).value, ((ExprNodeType)elementType).typespec));

    }

    protected final SymbolLocation makeSymLocation(Token token) {
        return ExpressionEval.makeSymLocation(unit.filename, token);
    }

    @Override
    public ExprNodeData visitTypeDeclAssign(MiodParser.TypeDeclAssignContext ctx) {
        // check if already defined
        String id = ctx.bareName().getText();
        SymbolLocation loc = makeSymLocation(ctx.getStart());
        SymbolTableItem sym = currentSymTable.resolve(id);
        if (sym != null) {
            context.getErrorListener().onError(new SymbolRedefinitionError(sym, loc));
            return null;
        }
        // TODO get visibility from context
        SymbolVisibility visibility = SymbolVisibility.PUBLIC;
        SymbolDesc desc = new SymbolDesc(id, loc, null, visibility);

        // TODO handle GENERIC, enumDecl
        if (ctx.arrayType() != null) {
            ExprNodeType arrayType = (ExprNodeType)visit(ctx.arrayType());
            if (arrayType != null) {
                desc.type = arrayType.typespec;
                currentSymTable.put(new TypeDefSymbol(desc));
            }
        }
        return null;
    }

    @Override
    public ExprNodeData visitExprCast(MiodParser.ExprCastContext ctx) {
        ExprNodeType targetTypeData = (ExprNodeType)visit(ctx.typeSpec());
        ExprNodeValue exprData = (ExprNodeValue)visit(ctx.expr());

        if (ExpressionEval.nulls(targetTypeData, exprData)) {
            return null;
        }

        if (exprData.value.getType().supportsCastTo(targetTypeData.typespec)) {
            return ExprNodeValue.newValue(exprData.value.castTo(targetTypeData.typespec));
        }

        context.getErrorListener().onError(new TypesMismatch(makeSymLocation(ctx.typeSpec().getStart())));
        /* TODO
            + targetType can convert?
            - log error if overflow?


            + long -> int
            - double -> float
            - base class -> concrete class

            - 1) convert types, insert checks in code generation
            + 2) convert values
         */

        return null;
    }

    @Override
    public ExprNodeData visitExprArray(MiodParser.ExprArrayContext ctx) {
        // TODO get type from the first expr, make sure the rest are of the same type
        ExprNodeData firstElem = visit(ctx.expr());

        if (firstElem == null) {
            return null;
        }

        if (firstElem instanceof ExprNodeValue == false) {
            context.getErrorListener().onError(new ValueExpected(makeSymLocation(ctx.expr().getStart())));
            return null;
        }

        final MiodType arrayItemType = ((ExprNodeValue)firstElem).value.getType();

        ExprNodeList nodeList = (ExprNodeList)visit(ctx.commaExpr());

        for(ExprNodeData i : nodeList.list) {
            if (i == null)
                return null;

            if (i instanceof ExprNodeValue == false) {
                context.getErrorListener().onError(new ValueExpected(makeSymLocation(ctx.commaExpr().getStart())));
                return null;
            }

            if (((ExprNodeValue)i).value.getType() != arrayItemType) {
                context.getErrorListener().onError(new TypesMismatch(makeSymLocation(ctx.commaExpr().getStart())));
                return null;
            }
        }

        MiodValue[] values = new MiodValue[nodeList.list.length + 1];
        values[0] = ((ExprNodeValue)firstElem).value;
        int vIndex = 1;
        for(ExprNodeData i : nodeList.list) {
            values[vIndex] = ((ExprNodeValue)i).value;
            ++vIndex;
        }

        return ExprNodeValue.newValue(new ArrayValue(new ArrayType(values.length, arrayItemType), values));
    }

    @Override
    public ExprNodeData visitCommaExpr(MiodParser.CommaExprContext ctx) {
        ExprNodeData[] nodes = new ExprNodeData[ctx.expr().size()];
        int nodeIndex = 0;
        for(MiodParser.ExprContext e : ctx.expr()) {
            nodes[nodeIndex] = visit(e);
            ++nodeIndex;
        }
        return new ExprNodeList(nodes); //To change body of generated methods, choose Tools | Templates.
    }



}
