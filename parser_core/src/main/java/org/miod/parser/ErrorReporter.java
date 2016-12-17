/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */

package org.miod.parser;

import java.util.ArrayList;
import java.util.List;
import org.miod.parser.visitors.SemanticVisitor;
import org.miod.program.errors.CompilerError;
import org.miod.program.errors.CompilerWarning;
import org.miod.program.symbol_table.SymbolLocation;

/**
 *
 * @author yur
 */
public final class ErrorReporter implements ErrorListener {
    private final List<CompilerError> errors = new ArrayList<>();
    private final List<CompilerWarning> warnings = new ArrayList<>();
    private boolean stopAtFirstError = false;

    public ErrorReporter() {

    }

    public void setStopAtFirstError(boolean b) {
        stopAtFirstError = b;
    }

    @Override
    public boolean hasErrors() {
        return errors.isEmpty() == false;
    }

    public void reset() {
        errors.clear();
        warnings.clear();
    }

    @Override
    public void onError(CompilerError e) {
        errors.add(e);
        if (stopAtFirstError)
            throw new ErrorReporterException();
    }
    @Override
    public void onWarning(CompilerWarning w) {
        warnings.add(w);
    }

    @Override
    public void onUnknownIdentifier(SemanticVisitor visitor, String name, SymbolLocation loc) {
        // TODO trigger error on second pass
    }


}
