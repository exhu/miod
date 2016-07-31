/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

/** Expression and symbol table entry types.
 *
 * @author yur
 */
public enum ValueTypeId {
    UNIT_DEF,
    //ALIAS,
    // TYPE_DEF,equals to alias, so should be removed from grammar,
    // use struct for distinct types.
    INT8,
    INT16,
    INT32,
    INT64,
    CARDINAL,
    FLOAT,
    DOUBLE,
    UINT8,
    UINT16,
    UINT32,
    UINT64,
    NCHAR_LITERAL,
    NCHAR,
    STRING_LITERAL,
    NSTRING,
    STRING,
    ARRAY_SIZED,
    ARRAY_REF,
    ENUM_DEF,
    ENUM_VALUE,
    STRUCT_DEF,
    STRUCT_REF,
    PROC_DEF,
    PROC_REF,
    CLASS_DEF,
    CLASS_REF,
    CLASS_WEAK,
    CLASS_WEAK_REF,
    METHOD_DEF,
    METHOD_WITH_INSTANCE_REF,
    VAR_REF,
    OPAQUE,
}

