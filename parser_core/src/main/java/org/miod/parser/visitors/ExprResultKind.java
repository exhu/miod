/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.visitors;

/** Result type for constant expressions evaluation.
 *
 * @author yur
 */
public enum ExprResultKind {
    UNKNOWN,
    VALUE,
    RUNTIME,
    GENERIC,
}
