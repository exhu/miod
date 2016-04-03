/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import org.miod.program.SymItem;

/** alias myname = othertype
 *
 * @author yur
 */
public final class AliasType extends AliasedType {
    AliasType(SymItem aliasFor) {
        super(aliasFor);
    }
}
