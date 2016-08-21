/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.UnitNotFoundError;

/** Top class to use. Called by ParserContext to resolve imports.
 * UnitParser.parseFile(filename) -> "import mypkg::myunit" -> 
 * -> ParserContext.parseUnit -> UnitParser.parseUnit
 *
 * @author yur
 */
public final class UnitParser implements UnitParserProvider {
    private final Set<Path> packagePaths = new HashSet<>();
    private final DefaultUnitsPathsResolver pathsResolver = new DefaultUnitsPathsResolver();
    private final ParserContext context = new ParserContext(this);    
    private final static Logger LOGGER = Logger.getLogger(UnitParser.class.getName());
    private ErrorListener errorListener;

    public UnitParser(List<String> packagePaths, ErrorListener errorListener) {
        this.errorListener = errorListener;
        pathsResolver.addPaths(packagePaths);
    }
    
    @Override
    public CompilationUnit parseUnit(String unitName) {        
        CompilationUnit unit = context.getUnit(unitName);
        if (unit == null) {
            // find file in paths
            final Path unitPath = pathsResolver.pathFromUnitName(unitName);
            if (unitPath == null) {
                errorListener.onError(new UnitNotFoundError(unitName));
            } else {
                unit = parseFile(unitPath);
            }
        }        
        return unit;
    }

    public CompilationUnit parseFile(Path unitPath) {
        final String unitName = pathsResolver.unitNameFromPath(unitPath);
        // check if already parsed in context
        CompilationUnit unit = context.getUnit(unitName);
        if (unit != null) {
            return unit;
        }
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
