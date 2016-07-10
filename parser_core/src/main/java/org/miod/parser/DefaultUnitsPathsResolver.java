/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.miod.program.symbol_table.BasicSymbolTable;
import org.miod.program.CompilationUnit;

/**
 *
 * @author yur
 */
public final class DefaultUnitsPathsResolver implements UnitsPathsResolver {
    private final Set<Path> packagePaths = new HashSet<>();
    private final static Logger LOGGER = Logger.getLogger(DefaultUnitsPathsResolver.class.getName());
    
    /// convert paths to absolute and canonical
    @Override
    public void addPaths(List<String> paths) {
        for(String s : paths) {
            Path p = FileSystems.getDefault().getPath(s).toAbsolutePath();
            LOGGER.log(Level.INFO, "Add package path {0}", p.toString());
            packagePaths.add(p);
        }
        // TODO exclude overlapping paths, e.g. 1) /libs/lib1 2) /libs/lib1/some
        // only the first packages path is valid!
    }
    
    /// return name based on the path and packagePaths
    @Override
    public String unitNameFromPath(Path p) {
        // trim package path
        final Path parent = p.toAbsolutePath().getParent();        
        Path trimmedPath = parent;
        for(Path pp : packagePaths) {
            if (parent.startsWith(pp)) {
                trimmedPath = pp.relativize(parent);                
            }
        }
        
        final StringBuilder builder = new StringBuilder();
        for(Path name : trimmedPath) {
            final String s = name.toString();            
            if (!s.isEmpty()) {
                builder.append(s);
                builder.append(BasicSymbolTable.NAMESPACE_SEP);
            }
        }
        
        String fileName = p.getFileName().toString();
        builder.append(fileName.substring(0, fileName.lastIndexOf('.')));
        return builder.toString();
    }
    
    /// searches file system for the unit
    @Override
    public Path unitNameToPath(String unitName) {        
        final String stringPath = unitName.replace(BasicSymbolTable.NAMESPACE_SEP,
                FileSystems.getDefault().getSeparator()) +
                        CompilationUnit.UNIT_FILENAME_SUFFIX;
        
        LOGGER.log(Level.INFO, "string path = {0}", stringPath);
        
        for(Path pkgpath : packagePaths) {
            Path joined = pkgpath.getFileSystem().getPath(pkgpath.toString(), stringPath);
            LOGGER.log(Level.INFO, "joined path = {0}", joined.toString());
            if (Files.isReadable(joined)) {
                return joined;
            }
        }
        
        return null;
    }

}
