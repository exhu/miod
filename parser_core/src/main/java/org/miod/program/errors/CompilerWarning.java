/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public abstract class CompilerWarning {
    protected String text;
    public CompilerWarning(String msg) {
        this.text = msg;
    }
}
