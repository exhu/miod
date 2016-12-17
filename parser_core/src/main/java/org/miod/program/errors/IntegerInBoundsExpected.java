/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.errors;

/**
 *
 * @author yur
 */
public final class IntegerInBoundsExpected extends CompilerError {
    public IntegerInBoundsExpected(int low, int high) {
        super(String.format("IntegerInBoundsExpected %d .. %d", low, high));
    }

}
