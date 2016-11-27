/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Base for standard types both public and implementation, e.g. WEAK refs, int8
 *
 * @author yur
 */
@Deprecated
public final class PrimitiveType extends MiodType {
    private PrimitiveType(ValueTypeId typeId) {
        super(typeId);
    }
    
    public static final PrimitiveType BOOL = new PrimitiveType(ValueTypeId.BOOL);    

    @Override
    public boolean supportsEqualOp() {
        return true;
    }

    @Override
    public boolean supportsLessThanOp() {
        return true;
    }
}
