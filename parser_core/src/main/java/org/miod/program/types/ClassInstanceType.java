/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public abstract class ClassInstanceType extends MiodType {
    public final RefVariant variant;

    public ClassInstanceType(RefVariant variant) {
        super(ValueTypeId.CLASS_INSTANCE);
        this.variant = variant;        
    }
}
