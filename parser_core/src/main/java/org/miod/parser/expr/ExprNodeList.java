/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

/**
 * A list of expressions, e.g. array literal or proc arguments enumeration.
 *
 * @author yur
 */
public final class ExprNodeList extends ExprNodeData {

    public final ExprNodeData[] list;

    public ExprNodeList(ExprNodeData[] list) {
        this.list = list;
    }
}
