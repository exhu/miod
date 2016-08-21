/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.miod.generated.MiodLexer;
import org.miod.generated.MiodParser;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.errors.CompilerIOError;
import org.miod.program.errors.UnitNotFoundError;

/**
 * Top class to use. Called by ParserContext to resolve imports.
 * UnitParser.parseFile(filename) -> "import mypkg::myunit" -> ->
 * ParserContext.parseUnit -> UnitParser.parseUnit
 *
 * @author yur
 */
public final class UnitParser implements UnitParserProvider {

    private final Set<Path> packagePaths = new HashSet<>();
    private final DefaultUnitsPathsResolver pathsResolver = new DefaultUnitsPathsResolver();
    private final ParserContext context = new ParserContext(this);
    private final static Logger LOGGER = Logger.getLogger(UnitParser.class.getName());
    private final ErrorListener errorListener;

    public UnitParser(List<String> packagePaths, ErrorListener errorListener) {
        this.errorListener = errorListener;
        pathsResolver.addPaths(packagePaths);
        context.setErrorListener(errorListener);
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

    public final CompilationUnit parseFile(Path unitPath) {
        final String unitName = pathsResolver.unitNameFromPath(unitPath);
        // check if already parsed in context
        CompilationUnit unit = context.getUnit(unitName);
        if (unit != null) {
            return unit;
        }

        ParseTree tree;
        ParserErrorListener antlrErrorListener = new ParserErrorListener(this.errorListener);
        try (BufferedReader reader = Files.newBufferedReader(unitPath,
                Charset.forName("UTF-8"))) {
            ANTLRInputStream input = new ANTLRInputStream(reader);
            MiodLexer lexer = new MiodLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiodParser parser = new MiodParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(antlrErrorListener);
            // run parser
            tree = parser.compUnit();
            LOGGER.log(Level.INFO, () -> tree.toStringTree(parser));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            errorListener.onError(new CompilerIOError());
            return null;
        }

        // stop if failed to parse input
        if (antlrErrorListener.hasErrors) {
            return null;
        }
        
        // TODO
        /*
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
