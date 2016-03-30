/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.compiler;

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
