/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.parser.ParserContext;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;
import org.miod.program.CompilationUnit;

/** First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<Object>{
    protected final ParserContext context;
    protected CompilationUnit unit;
    protected final String unitName;
    
    public SemanticVisitor(String unitName, ParserContext ctx) {
        this.context = ctx;
        this.unitName = unitName;
    }
    
    @Override
    public Object visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        Object res = visit(ctx.boolExpr());
        // TODO if true, visit(ctx.trueStmts), else ctx.falseStmts
        return res;
    }
    
}
