/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Base for standard types both public and implementation, e.g. WEAK refs, int8
 *
 * @author yur
 */
public final class PrimitiveType extends MiodType {
    public PrimitiveType(ValueTypeId typeId) {
        super(typeId);
    }

}
