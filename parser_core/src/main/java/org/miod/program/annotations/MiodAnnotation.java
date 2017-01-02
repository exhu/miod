/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;

/**
 *
 * @author yur
 */
public abstract class MiodAnnotation {
    private final String name;
    public MiodAnnotation(String name) {
        this.name = name;
    }
}
