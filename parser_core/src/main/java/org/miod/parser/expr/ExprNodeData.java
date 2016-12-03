/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.expr;

import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.symbols.ConstSymbol;
import org.miod.program.symbol_table.symbols.TypeDefSymbol;
import org.miod.program.types.MiodType;
import org.miod.program.values.BoolValue;
import org.miod.program.values.MiodValue;
import org.miod.program.values.NullValue;

/**
 *
 * @author yur
 */

public final class ExprNodeData {
    /// Can be null for the first pass, e.g. yet unknown identifiers,
    /// error if null for the second pass.
    /// Contains explicit value for literals and through constants expansion,
    /// RuntimeValue for vars, function calls, array indexing etc.
    /// typespec for type name etc. e.g. const a : int = 3, 'int' -> MiodType
    // TODO store values for common types and values and return constants.
    public final MiodValue value;
    public final MiodType typespec;

    public static ExprNodeData newValue(MiodValue v) {
        if (v == NullValue.VALUE)
            return VALUE_NULL;
        if (v == BoolValue.FALSE)
            return VALUE_FALSE;
        if (v == BoolValue.TRUE)
            return VALUE_TRUE;
        
        return new ExprNodeData(v, null);
    }

    public static ExprNodeData newTypespec(MiodType t) {
        return new ExprNodeData(null, t);
    }

    public static ExprNodeData newFromSymbolTableItem(SymbolTableItem sym) {
        if (sym instanceof ConstSymbol) {
            return newValue(((ConstSymbol)sym).value);
        } else if (sym instanceof TypeDefSymbol) {
            return newTypespec(((TypeDefSymbol)sym).desc.type);
        }

        return null;
    }

    private final static ExprNodeData VALUE_NULL = new ExprNodeData(NullValue.VALUE, null);
    private final static ExprNodeData VALUE_FALSE = new ExprNodeData(BoolValue.FALSE, null);
    private final static ExprNodeData VALUE_TRUE = new ExprNodeData(BoolValue.TRUE, null);

    private ExprNodeData(MiodValue v, MiodType t) {
        value = v;
        typespec = t;
    }
}
