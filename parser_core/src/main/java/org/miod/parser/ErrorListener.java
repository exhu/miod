/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import org.miod.program.SymLocation;

/** Methods return true to continue parsing.
 *
 * @author yur
 */
public interface ErrorListener {
    
    void onError(CompilerError e);
    void onWarning(CompilerWarning w);
    /*
    void onUnknownIdentifier(String name, SymLocation loc);
    void onTypeMismatch(SymLocation loc);
    void onRedeclaration(String name, SymLocation loc);
    void onSyntaxError(String msg);
    */
}
