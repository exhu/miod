/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.parser;

import java.util.ArrayList;
import java.util.List;
import org.miod.program.errors.CompilerError;
import org.miod.program.errors.CompilerWarning;

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

    @Override
    public void onError(CompilerError e) {
        // TODO
    }
    @Override
    public void onWarning(CompilerWarning w) {
        // TODO
    }
}
