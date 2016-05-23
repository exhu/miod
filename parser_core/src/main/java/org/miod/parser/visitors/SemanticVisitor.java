/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.parser.ParserContext;
import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;

/** First pass visitor. Gathers declarations, tries to evaluate certain
 * expressions.
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<Object>{
    protected ParserContext context;
    
    public SemanticVisitor(ParserContext ctx) {
        this.context = ctx;
    }
    
    @Override
    public Object visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        Object res = visit(ctx.boolExpr());
        // TODO if true, visit(ctx.trueStmts), else ctx.falseStmts
        return res;
    }
    
}
