/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.miod.program.CompilationUnit;

/** Maintains all processed units, predefined directives, options etc.
 *
 * @author yur
 */
public final class ParserContext {
    private Set<CompilationUnit> units = new HashSet<>();
    private Set<String> packagesPaths = new HashSet<>();
    private Map<String, String> globalDefines = new HashMap<>();
}
