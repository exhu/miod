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
import org.miod.parser.visitors.SemanticResolverVisitor;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.errors.CompilerIOError;
import org.miod.program.errors.UnitNotFoundError;
import org.miod.parser.generated.*;

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

    public ParserContext getContext() {
        return context;
    }

    @Override
    public void parseUnit(String unitName) {
        // find file in paths
        final Path unitPath = pathsResolver.pathFromUnitName(unitName);
        if (unitPath == null) {
            errorListener.onError(new UnitNotFoundError(unitName));
        } else {
            parseFile(unitName, unitPath, false);
        }        
    }

    public final void parseFile(String unitName, Path unitPath, boolean singlePass) {
        //final String unitName = pathsResolver.unitNameFromPath(unitPath);
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
            return;
        }

        // stop if failed to parse input
        if (antlrErrorListener.hasErrors) {
            return;
        }

        // declaration pass
        SemanticVisitor visitor1 = new SemanticVisitor(unitName, context);
        visitor1.visit(tree);

        if (errorListener.hasErrors() == false) {
            context.putTree(unitName, tree);
            if (singlePass == false) {
                // definition pass
                SemanticResolverVisitor visitor2 = new SemanticResolverVisitor(unitName, context);
                visitor2.visit(tree);
            }
        }
    }
}
