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

    public ClassInstanceType(ValueTypeId typeId) {
        super(typeId);
        switch(typeId) {
            case CLASS_REF:
                variant = RefVariant.STRONG;
                break;
            case CLASS_WEAK:
                variant = RefVariant.WEAK;
                break;
            case CLASS_WEAK_REF:
                variant = RefVariant.WEAK_WATCH;
                break;
            default: variant = null;
        }
    }
}
