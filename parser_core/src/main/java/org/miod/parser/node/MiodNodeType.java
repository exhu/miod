/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

import org.miod.program.types.MiodType;

/**
 *
 * @author yur
 */
public final class MiodNodeType extends MiodNodeData {
    public final MiodType typespec;

    public static MiodNodeType newTypespec(MiodType t) {
        return new MiodNodeType(t);
    }

    private MiodNodeType(MiodType t) {
        this.typespec = t;
    }
}
