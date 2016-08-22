/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import org.miod.program.errors.CompilerError;
import org.miod.program.errors.CompilerWarning;

/** Methods return true to continue parsing.
 *
 * @author yur
 */
public interface ErrorListener {

    boolean hasErrors();
    void onError(CompilerError e);
    void onWarning(CompilerWarning w);
    /*
    void onUnknownIdentifier(String name, SymLocation loc);
    void onTypeMismatch(SymLocation loc);
    void onRedeclaration(String name, SymLocation loc);
    void onSyntaxError(String msg);
    */
}
