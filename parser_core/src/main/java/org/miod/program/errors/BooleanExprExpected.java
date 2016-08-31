/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public final class BooleanExprExpected extends CompilerError {
    public BooleanExprExpected() {
        super("Boolean expression expected.");
    }

}
