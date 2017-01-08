/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import java.util.Map;

/**
 *
 * @author yur
 */
public final class ExprNodeDict extends ExprNodeData {
    public final Map<String, ExprNodeData> map;
    public ExprNodeDict(Map<String, ExprNodeData> map) {
        this.map = map;
    }

}
