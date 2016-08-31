/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public abstract class CompilerError {
    protected String text;
    public CompilerError(String msg) {
        this.text = msg;
    }

}
