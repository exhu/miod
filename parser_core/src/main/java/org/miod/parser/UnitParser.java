/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.AccessMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.BaseSymbolTable;
import org.miod.program.CompilationUnit;

/** Top class to use. Called by ParserContext to resolve imports.
 * UnitParser.parseFile(filename) -> "import mypkg::myunit" -> 
 * -> ParserContext.parseUnit -> UnitParser.parseUnit
 *
 * @author yur
 */
public final class UnitParser implements UnitParserProvider {
    private final Set<Path> packagePaths = new HashSet<>();
    private final ParserContext context = new ParserContext(this);
    private boolean terminateOnFirstError = false;
    private final static Logger LOGGER = Logger.getLogger(UnitParser.class.getName());

    public UnitParser(List<String> packagePaths) {        
        addPaths(packagePaths);        
    }
    
    /// convert paths to absolute and canonical
    private void addPaths(List<String> paths) {
        for(String s : paths) {
            Path p = FileSystems.getDefault().getPath(s).toAbsolutePath();
            LOGGER.log(Level.INFO, "Add package path {0}", p.toString());
            packagePaths.add(p);
        }
        // TODO exclude overlapping paths, e.g. 1) /libs/lib1 2) /libs/lib1/some
        // only the first packages path is valid!
    }
    
    /// return name based on the path and packagePaths
    public final String unitNameFromPath(Path p) {        
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
                builder.append(BaseSymbolTable.NAMESPACE_SEP);
            }
        }
        
        String fileName = p.getFileName().toString();
        builder.append(fileName.substring(0, fileName.lastIndexOf('.')));
        return builder.toString();
    }
    
    /// searches file system for the unit
    public final Path unitNameToPath(String unitName) {        
        final String stringPath = unitName.replace(BaseSymbolTable.NAMESPACE_SEP,
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

    @Override
    public CompilationUnit parseUnit(String unitName) {
        // TODO find file in paths
        // TODO run parseFile
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CompilationUnit parseFile(Path f, ErrorListener elistener) {
        String unitName = "";
        // TODO path to absolute
        // TODO unitNameFromPath
        // TODO unitName to relative path
        //Path unitPath = FileSystems.getDefault().getPath(unitName);

        /*
         try (BufferedReader reader = Files.newBufferedReader(f,
                Charset.forName("UTF-8"))) {
            ANTLRInputStream input = new ANTLRInputStream(reader);
            MyCalcLexer lexer = new MyCalcLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MyCalcParser parser = new MyCalcParser(tokens);
            parser.removeErrorListeners();
            ParserErrorListener errorListener = new ParserErrorListener(reporter);
            parser.addErrorListener(errorListener);
            tree = parser.compUnit();
            logger.info(tree.toStringTree(parser));
        }

        if (reporter.hasErrors()) {
            reportErrors(reporter.getErrorsCount());
            return reporter;
        }

        // declaration pass
        MyCalcDeclListener declLst = new MyCalcDeclListener(reporter, treeCtx);
        try {
            walker.walk(declLst, tree);
        }
        catch(SemanticError e) {
            if (rethrow) {
                throw e;
            }
        }

        // definition pass
        MyCalcImplListener implLst = new MyCalcImplListener(reporter, treeCtx);
        try {
            walker.walk(implLst, tree);
        }
        catch(SemanticError e) {
            if (rethrow) {
                throw e;
            }
        }

        return reporter;
         */
        SemanticVisitor visitor1 = new SemanticVisitor(unitName, context);
        // TODO visit tree
        return null;
    }    
}
