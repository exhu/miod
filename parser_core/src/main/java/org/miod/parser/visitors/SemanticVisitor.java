/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.Token;
import org.miod.parser.ParserContext;
import org.miod.parser.node.MiodNodeData;
import org.miod.parser.node.MiodNodeDict;
import org.miod.parser.node.MiodNodeList;
import org.miod.parser.node.MiodNodeNameData;
import org.miod.parser.node.MiodNodeType;
import org.miod.parser.node.MiodNodeValue;
import org.miod.parser.expr.ExprEvalHelpers;
import static org.miod.parser.expr.ExprEvalHelpers.exprEq;
import static org.miod.parser.expr.ExprEvalHelpers.exprLess;
import static org.miod.parser.expr.ExprEvalHelpers.exprLessOrEqual;
import static org.miod.parser.expr.ExprEvalHelpers.exprPlus;
import static org.miod.parser.expr.ExprEvalHelpers.invertBool;
import org.miod.parser.node.MiodNodeAnnotation;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.annotations.MiodAnnotationHelpers;
import org.miod.program.errors.BooleanExprExpected;
import org.miod.program.errors.CompileTimeExpressionExpected;
import org.miod.program.errors.DuplicateKey;
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
public class SemanticVisitor extends MiodParserBaseVisitor<MiodNodeData> {

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
    public MiodNodeData visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        MiodValue res = ((MiodNodeValue) visit(ctx.boolExpr())).value;
        if (res instanceof BoolValue) {
            if (((BoolValue) res).value == true) {
                return visit(ctx.trueStmts);
            } else {
                return visit(ctx.falseStmts);
            }
        } else if (res instanceof RuntimeValue) {
            context.getErrorListener().onError(
                    new CompileTimeExpressionExpected(makeSymLocation(ctx.boolExpr().getStart())));
        } else {
            context.getErrorListener().onError(new BooleanExprExpected(
                    makeSymLocation(ctx.boolExpr().getStart())));
        }
        return null;
    }

    @Override
    public MiodNodeData visitUnitHeader(MiodParser.UnitHeaderContext ctx) {
        unit = new CompilationUnit(context.getDefaultSymbolTable(), unitName,
                0, 0, unitName, context.getErrorListener());
        context.putUnit(unitName, unit);
        currentSymTable = unit.symTable;
        return super.visitUnitHeader(ctx);
    }

    @Override
    public MiodNodeData visitLiteralNull(MiodParser.LiteralNullContext ctx) {
        return MiodNodeValue.newValue(NullValue.INSTANCE);
    }

    @Override
    public MiodNodeData visitLiteralFalse(MiodParser.LiteralFalseContext ctx) {
        return MiodNodeValue.newValue(BoolValue.FALSE);
    }

    @Override
    public MiodNodeData visitLiteralTrue(MiodParser.LiteralTrueContext ctx) {
        return MiodNodeValue.newValue(BoolValue.TRUE);
    }

    @Override
    public MiodNodeData visitLiteralInteger(MiodParser.LiteralIntegerContext ctx) {
        return MiodNodeValue.newValue(new IntegerValue(Long.parseLong(ctx.INTEGER().getText())));
    }

    @Override
    public MiodNodeData visitLiteralCharStr(MiodParser.LiteralCharStrContext ctx) {
        // TODO strip quotes, return uint8
        return super.visitLiteralCharStr(ctx);
    }

    @Override
    public MiodNodeData visitLiteralString(MiodParser.LiteralStringContext ctx) {
        return MiodNodeValue.newValue(new StringValue(
                ExprEvalHelpers.extractStringFromLiteral(ctx.STRING().getText())));
    }

    @Override
    public MiodNodeData visitExprLiteral(MiodParser.ExprLiteralContext ctx) {
        return visit(ctx.literal());
    }

    @Override
    public MiodNodeData visitExprGreater(MiodParser.ExprGreaterContext ctx) {
        return MiodNodeValue.newValue(invertBool(exprLessOrEqual(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart()))));
    }

    @Override
    public MiodNodeData visitExprLess(MiodParser.ExprLessContext ctx) {
        return MiodNodeValue.newValue(exprLess(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public MiodNodeData visitExprGreaterEq(MiodParser.ExprGreaterEqContext ctx) {
        return MiodNodeValue.newValue(exprLessOrEqual(((MiodNodeValue) visit(ctx.right)).value,
                ((MiodNodeValue) visit(ctx.left)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public MiodNodeData visitExprLessEq(MiodParser.ExprLessEqContext ctx) {
        return MiodNodeValue.newValue(exprLessOrEqual(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public MiodNodeData visitExprEquals(MiodParser.ExprEqualsContext ctx) {
        return MiodNodeValue.newValue(exprEq(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart())));
    }

    @Override
    public MiodNodeData visitExprNotEq(MiodParser.ExprNotEqContext ctx) {
        return MiodNodeValue.newValue(invertBool(exprEq(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(), makeSymLocation(ctx.getStart()))));
    }

    // TODO special case for stuct recursion in definition, e.g.
    // 1) type mystruct = struct parent: mystruct end_struct --
    // remember typename
    // 2) type myclass = class parent: myclass end_class
    // -- class type is mutable, so it's put into parent SymbolTable and
    // filled as parsing goes further.
    @Override
    public MiodNodeData visitConstExpr(MiodParser.ConstExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public MiodNodeData visitConstAssign(MiodParser.ConstAssignContext ctx) {
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

        MiodNodeValue nodeValue = (MiodNodeValue) visit(ctx.constExpr());
        MiodValue value = nodeValue.value;
        // create symbol, put to table
        if (ctx.typeSpec() != null) {
            // check type of the expr
            LOGGER.log(Level.INFO, "typed const");
            MiodNodeType typeSpec = (MiodNodeType) visit(ctx.typeSpec());

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

    protected final MiodNodeData resolveSymbol(String name, SymbolLocation loc) {
        // current context local symbol table
        SymbolTableItem sym = currentSymTable.resolve(name);
        if (sym != null) {
            return MiodNodeData.newFromSymbolTableItem(sym);
        }
        containsUnresolvedSyms = true;
        context.getErrorListener().onUnknownIdentifier(this, name, loc);
        return null;
    }

    @Override
    public MiodNodeData visitQualifName(MiodParser.QualifNameContext ctx) {
        return resolveSymbol(ctx.getText(), makeSymLocation(ctx.getStart()));
    }

    @Override
    public MiodNodeData visitExprQualifName(MiodParser.ExprQualifNameContext ctx) {
        return visit(ctx.qualifName());
    }

    @Override
    public MiodNodeData visitTypeSpecName(MiodParser.TypeSpecNameContext ctx) {
        return resolveSymbol(ctx.qualifName().getText(), makeSymLocation(ctx.qualifName().getStart()));
    }

    @Override
    public MiodNodeData visitExprParen(MiodParser.ExprParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public MiodNodeData visitExprPlus(MiodParser.ExprPlusContext ctx) {
        return MiodNodeValue.newValue(exprPlus(((MiodNodeValue) visit(ctx.left)).value,
                ((MiodNodeValue) visit(ctx.right)).value,
                context.getErrorListener(),
                makeSymLocation(ctx.getStart())));
    }

    @Override
    public MiodNodeData visitTypeSpecArray(MiodParser.TypeSpecArrayContext ctx) {
        return visit(ctx.arrayType());
    }

    @Override
    public MiodNodeData visitArrayType(MiodParser.ArrayTypeContext ctx) {
        return visit(ctx.arrayVariant());
    }

    @Override
    public MiodNodeData visitUnknownSizeArray(MiodParser.UnknownSizeArrayContext ctx) {
        MiodNodeData elementType = visit(ctx.qualifName());
        if (elementType == null) {
            return null;
        }

        // expect type name
        if (elementType instanceof MiodNodeType == false) {
            context.getErrorListener().onError(new TypeNameExpected(
                    makeSymLocation(ctx.qualifName().getStart())));
            return null;
        }

        return MiodNodeType.newTypespec(ArrayRefType.fromMiodType(((MiodNodeType) elementType).typespec));
    }

    @Override
    public MiodNodeData visitSizedArray(MiodParser.SizedArrayContext ctx) {
        MiodNodeData elementType = visit(ctx.typeSpec());
        if (elementType == null) {
            return null;
        }

        // expect type name
        if (elementType instanceof MiodNodeType == false) {
            context.getErrorListener().onError(new TypeNameExpected(
                    makeSymLocation(ctx.typeSpec().getStart())));
            return null;
        }

        MiodNodeValue sizeSpec = (MiodNodeValue) visit(ctx.expr());
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

        return MiodNodeType.newTypespec(new ArrayType((int) ((IntegerValue) sizeSpec.value).value, ((MiodNodeType) elementType).typespec));

    }

    protected final SymbolLocation makeSymLocation(Token token) {
        return ExprEvalHelpers.makeSymLocation(unit.filename, token);
    }

    @Override
    public MiodNodeData visitTypeDeclAssign(MiodParser.TypeDeclAssignContext ctx) {
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
            MiodNodeType arrayType = (MiodNodeType) visit(ctx.arrayType());
            if (arrayType != null) {
                desc.type = arrayType.typespec;
                currentSymTable.put(new TypeDefSymbol(desc));
            }
        }
        return null;
    }

    @Override
    public MiodNodeData visitExprCast(MiodParser.ExprCastContext ctx) {
        MiodNodeType targetTypeData = (MiodNodeType) visit(ctx.typeSpec());
        MiodNodeValue exprData = (MiodNodeValue) visit(ctx.expr());

        if (ExprEvalHelpers.nulls(targetTypeData, exprData)) {
            return null;
        }

        if (exprData.value.getType().supportsCastTo(targetTypeData.typespec)) {
            return MiodNodeValue.newValue(exprData.value.castTo(targetTypeData.typespec));
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
    public MiodNodeData visitExprArray(MiodParser.ExprArrayContext ctx) {
        // TODO get type from the first expr, make sure the rest are of the same type
        MiodNodeData firstElem = visit(ctx.expr());

        if (firstElem == null) {
            return null;
        }

        if (firstElem instanceof MiodNodeValue == false) {
            context.getErrorListener().onError(new ValueExpected(makeSymLocation(ctx.expr().getStart())));
            return null;
        }

        final MiodType arrayItemType = ((MiodNodeValue) firstElem).value.getType();

        MiodNodeList nodeList = (MiodNodeList) visit(ctx.commaExpr());

        for (MiodNodeData i : nodeList.list) {
            if (i == null) {
                return null;
            }

            if (i instanceof MiodNodeValue == false) {
                context.getErrorListener().onError(new ValueExpected(makeSymLocation(ctx.commaExpr().getStart())));
                return null;
            }

            if (((MiodNodeValue) i).value.getType() != arrayItemType) {
                context.getErrorListener().onError(new TypesMismatch(makeSymLocation(ctx.commaExpr().getStart())));
                return null;
            }
        }

        MiodValue[] values = new MiodValue[nodeList.list.length + 1];
        values[0] = ((MiodNodeValue) firstElem).value;
        int vIndex = 1;
        for (MiodNodeData i : nodeList.list) {
            values[vIndex] = ((MiodNodeValue) i).value;
            ++vIndex;
        }

        return MiodNodeValue.newValue(new ArrayValue(new ArrayType(values.length, arrayItemType), values));
    }

    @Override
    public MiodNodeData visitCommaExpr(MiodParser.CommaExprContext ctx) {
        MiodNodeData[] nodes = new MiodNodeData[ctx.expr().size()];
        int nodeIndex = 0;
        for (MiodParser.ExprContext e : ctx.expr()) {
            nodes[nodeIndex] = visit(e);
            ++nodeIndex;
        }
        return new MiodNodeList(nodes);
    }

    @Override
    public MiodNodeData visitAnnotationDictValue(MiodParser.AnnotationDictValueContext ctx) {
        return new MiodNodeNameData(ctx.bareName().getText(), visit(ctx.constExpr()));
    }

    @Override
    public MiodNodeData visitAnnotationDictValues(MiodParser.AnnotationDictValuesContext ctx) {
        MiodNodeData[] list = new MiodNodeData[ctx.annotationDictValue().size()];
        int index = 0;
        for (MiodParser.AnnotationDictValueContext i : ctx.annotationDictValue()) {
            list[index] = visit(i);
            ++index;
        }

        return new MiodNodeList(list);
    }

    @Override
    public MiodNodeData visitAnnotationDict(MiodParser.AnnotationDictContext ctx) {
        // check for duplicate keys!
        if (ctx.annotationDictValues() == null) {
            HashMap<String, MiodNodeData> dict = new HashMap<>(1);
            dict.put(ctx.bareName().getText(), visit(ctx.constExpr()));
            return new MiodNodeDict(dict);
        }

        MiodNodeList list = (MiodNodeList) visit(ctx.annotationDictValues());

        HashMap<String, MiodNodeData> dict = new HashMap<>(1 + list.list.length);
        for (MiodNodeData i : list.list) {
            MiodNodeNameData pair = (MiodNodeNameData) i;
            if (dict.containsKey(pair.name)) {
                context.getErrorListener().onError(new DuplicateKey(
                        makeSymLocation(ctx.annotationDictValues().getStart()),
                        pair.name));
                return null;
            }

            dict.put(pair.name, pair.data);
        }

        return new MiodNodeDict(dict);
    }

    @Override
    public MiodNodeData visitAnnotation(MiodParser.AnnotationContext ctx) {
        final String name = ctx.qualifName().getText();
        MiodNodeDict dict = null;
        SymbolLocation loc = makeSymLocation(ctx.start);
        if (ctx.annotationDict() != null) {
            dict = (MiodNodeDict) visit(ctx.annotationDict());
        }

        if (MiodAnnotationHelpers.isBuiltin(name)) {
            return new MiodNodeAnnotation(MiodAnnotationHelpers.newBuiltin(name,
                    dict, context.getErrorListener(), loc));
        }

        // TODO qualif name to symbol
        // TODO support user annotations
        return null;
    }

    @Override
    public MiodNodeData visitAnnotations(MiodParser.AnnotationsContext ctx) {
        MiodNodeData[] list = new MiodNodeData[ctx.annotation().size()];
        int index = 0;
        for (MiodParser.AnnotationContext i : ctx.annotation()) {
            list[index] = visit(i);
        }
        return new MiodNodeList(list);
    }

    @Override
    public MiodNodeData visitStatementAssign(MiodParser.StatementAssignContext ctx) {
        MiodNodeData left = visit(ctx.left);
        MiodNodeData right = visit(ctx.right);
        if (ExprEvalHelpers.nulls(left, right)) {
            return null;
        }

        /* TODO
            Can assign to:
            - variable
            - member variable
            - class property

            Call type assignment proc?
        */
        return null;
    }

    @Override
    public MiodNodeData visitStatementCall(MiodParser.StatementCallContext ctx) {
        MiodNodeData callable = visit(ctx.callable);
        if (callable == null) {
            return null;
        }

        /* TODO check if left:
            - proc
            - method
        */
        return null;
    }



}
