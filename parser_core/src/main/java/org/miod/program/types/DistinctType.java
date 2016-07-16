/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/**
 * type mytype = othertype
 *
 * @author yur
 */
public final class DistinctType extends AliasedType {

    DistinctType(MiodType aliasFor) {
        super(aliasFor, ValueTypeId.TYPE_DEF);
    }
}
