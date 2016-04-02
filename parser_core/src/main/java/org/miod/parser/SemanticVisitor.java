/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import org.miod.parser.generated.MiodParser;
import org.miod.parser.generated.MiodParserBaseVisitor;

/**
 *
 * @author yur
 */
public class SemanticVisitor extends MiodParserBaseVisitor<Object>{

    @Override
    public Object visitGlobalStaticIf(MiodParser.GlobalStaticIfContext ctx) {
        Object res = visit(ctx.boolExpr());
        // TODO if true, visit(ctx.trueStmts), else ctx.falseStmts
        return res;
    }
    
}
