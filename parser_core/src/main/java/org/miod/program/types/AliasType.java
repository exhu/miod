/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** alias myname = othertype
 *
 * @author yur
 */
public final class AliasType extends AliasedType {
    AliasType(MiodType aliasFor) {
        super(aliasFor, ValueTypeId.ALIAS);
    }
}
