/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Base for standard types both public and implementation, e.g. WEAK refs.
 *
 * @author yur
 */
public class BuiltinType extends MiodType {
    public BuiltinType(ValueTypeId typeId) {
        super(typeId);
    }

}
