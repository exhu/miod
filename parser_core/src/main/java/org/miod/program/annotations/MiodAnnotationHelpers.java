/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;

import org.miod.parser.expr.ExprNodeDict;

/**
 *
 * @author yur
 */
public final class MiodAnnotationHelpers {

    private MiodAnnotationHelpers() {
    }

    static public boolean isBuiltin(String name) {
        return name.equals(CAttrAnnotation.NAME);
    }

    static public MiodBuiltinAnnotation newBuiltin(String name, ExprNodeDict dict) {
        if (name.equals(CAttrAnnotation.NAME)) {
            // TODO
        }
        return null;
    }
}
