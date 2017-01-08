/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

import org.miod.program.annotations.MiodAnnotation;

/**
 *
 * @author yur
 */
public final class MiodNodeAnnotation extends MiodNodeData {
    public final MiodAnnotation annotation;
    public MiodNodeAnnotation(MiodAnnotation annotation) {
        this.annotation = annotation;
    }

}
