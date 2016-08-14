/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.Path;
import java.util.List;

public interface UnitsPathsResolver {
    /// add root paths to look for units
    void addPaths(List<String> paths);
    /// return name based on the path and packagePaths
    String unitNameFromPath(Path p);
    /// searches file system for the unit
    Path pathFromUnitName(String unitName);
}
