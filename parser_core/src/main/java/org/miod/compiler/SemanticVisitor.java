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
        return super.visitGlobalStaticIf(ctx); //To change body of generated methods, choose Tools | Templates.
    }
    
}
