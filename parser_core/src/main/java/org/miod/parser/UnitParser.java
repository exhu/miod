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
    private boolean terminateOnFirstError = false;
    private final static Logger LOGGER = Logger.getLogger(UnitParser.class.getName());

    public UnitParser(List<String> packagePaths) {        
        pathsResolver.addPaths(packagePaths);        
    }
    
    @Override
    public CompilationUnit parseUnit(String unitName) {
        // TODO find file in paths
        // TODO run parseFile
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CompilationUnit parseFile(Path f, ErrorListener elistener) {
        // TODO check if already parsed in context
        // TODO pass self as ErrorListener to CompilationUnit etc.
        // TODO decouple ParserContext from UnitParser
        // TODO special case for stuct recursion in definition, e.g.
        // 1) type mystruct = struct parent: mystruct end_struct -- 
        // remember typename
        // 2) type myclass = class parent: myclass end_class
        // -- class type is mutable, so it's put into parent SymbolTable and
        // filled as parsing goes further.
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
