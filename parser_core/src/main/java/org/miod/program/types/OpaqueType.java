/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 *
 * @author yur
 */
public final class OpaqueType extends MiodType {
    public final String translatedName;
    public OpaqueType(String translatedName) {
        super(ValueTypeId.OPAQUE);
        this.translatedName = translatedName;
    }
    
}
