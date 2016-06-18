/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.CompilationUnit;
import org.miod.program.SymItem;

/** Maintains all processed units, predefined directives, options etc.
 *
 * @author yur
 */
public final class ParserContext {
    private Set<CompilationUnit> units = new HashSet<>();
    private Set<String> packagePaths = new HashSet<>();
    /* Global defines are bad thing
    private Map<String, SymItem> globalDefines = new HashMap<>();
    better way is to have a miod.compiler.config module generated by
    compiler containing platform constants, e.g. debug mode or ref counting,
    cpu etc.
    */
    public ParserContext(List<String> packagePaths) {
        this.packagePaths.addAll(packagePaths);
    }
    
    public CompilationUnit parseUnit(String fn) {
        Path unitPath = FileSystems.getDefault().getPath(fn);

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

        SemanticVisitor visitor1 = new SemanticVisitor(this);
        // TODO visit tree
        return null;
    }
}
