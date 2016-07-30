/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class MiodType {
    public final ValueTypeId typeId;
    public MiodType(ValueTypeId typeId) {
        this.typeId = typeId;
    }

    /** TODO remove. Alias resolution on symbol table level. If this type is an alias or typedef returns the underlying type.
     * @return Super type.
     */
    @Deprecated
    public MiodType resolve() {
        return this;
    }

}
