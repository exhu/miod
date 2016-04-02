/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/** alias myname = othertype
 *
 * @author yur
 */
public final class TypeAlias extends AliasedType {
    TypeAlias(SymItem aliasFor) {
        super(aliasFor);
    }
}
