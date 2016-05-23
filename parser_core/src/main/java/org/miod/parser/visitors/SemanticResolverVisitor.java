/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

import org.miod.parser.ParserContext;

/** Second pass visitor. Undefined or unevaluated expressions trigger error.
 *
 * @author yur
 */
public class SemanticResolverVisitor extends SemanticVisitor {
    public SemanticResolverVisitor(ParserContext ctx) {
        super(ctx);
    }
}
