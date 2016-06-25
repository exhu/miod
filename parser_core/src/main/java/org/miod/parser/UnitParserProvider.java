/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import org.miod.program.CompilationUnit;

/**
 *
 * @author yur
 */
public interface UnitParserProvider {
    CompilationUnit parseUnit(String unitName);
}
