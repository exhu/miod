/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.CompilationUnit;

/** Top class to use. Called by ParserContext to resolve imports.
 * UnitParser.parseFile(filename) -> "import mypkg::myunit" -> 
 * -> ParserContext.parseUnit -> UnitParser.parseUnit
 *
 * @author yur
 */
public final class UnitParser implements UnitParserProvider {
    private final Set<String> packagePaths = new HashSet<>();
    private final ParserContext context;
    private boolean terminateOnFirstError = false;

    public UnitParser(ParserContext ctx, List<String> packagePaths) {
        this.context = ctx;
        // TODO convert paths to absolute and canonical
        this.packagePaths.addAll(packagePaths);
    }
    
    private String unitNameFromPath(Path p) {
        // TODO return name based on the path and packagePaths
        return "";
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
