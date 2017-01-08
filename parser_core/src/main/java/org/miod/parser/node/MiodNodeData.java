/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.symbols.ConstSymbol;
import org.miod.program.symbol_table.symbols.TypeDefSymbol;

/**
 *
 * @author yur
 */
public abstract class MiodNodeData {
    /// Can be null for the first pass, e.g. yet unknown identifiers,
    /// error if null for the second pass.
    /// Contains explicit value for literals and through constants expansion,
    /// RuntimeValue for vars, function calls, array indexing etc.
    /// typespec for type name etc. e.g. const a : int = 3, 'int' -> MiodType
    // TODO store values for common types and values and return constants.

    public static MiodNodeData newFromSymbolTableItem(SymbolTableItem sym) {
        if (sym instanceof ConstSymbol) {
            return MiodNodeValue.newValue(((ConstSymbol) sym).value);
        } else if (sym instanceof TypeDefSymbol) {
            return MiodNodeType.newTypespec(((TypeDefSymbol) sym).desc.type);
        }

        return null;
    }
}
