/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

/**
 *
 * @author yur
 */
public final class ExprNodeNameData extends ExprNodeData {
    public final String name;
    public final ExprNodeData data;
    public ExprNodeNameData(String name, ExprNodeData data) {
        this.name = name;
        this.data = data;
    }
}
