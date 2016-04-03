/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Int, float etc.
 *
 * @author yur
 */
public final class PrimitiveType {
    final public PrimitiveKind kind;
    public PrimitiveType(PrimitiveKind kind) {
        this.kind = kind;
    }
}
