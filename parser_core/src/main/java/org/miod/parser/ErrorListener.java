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
    boolean onUnknownVariable(String name, SymLocation loc);
    
}
