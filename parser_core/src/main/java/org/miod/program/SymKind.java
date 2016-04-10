/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program;

/** Symbol table types.
 *
 * @author yur
 */
public enum SymKind {
    Unit, // for imports
    Type,
    Alias,
    Var,
    Const,
    Annotation,
    Proc,
    Method
}
