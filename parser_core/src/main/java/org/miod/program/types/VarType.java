/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 * Argument to proc passed by reference
 *
 * @author yur
 */
public final class VarType extends MiodType {

    public final MiodType ref;

    public VarType(MiodType ref) {
        super(ValueTypeId.VAR_REF);
        this.ref = ref;
    }

}
