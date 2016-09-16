/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser;

import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.miod.program.errors.SyntaxError;

/**
 * ANTLR-based listener for syntax problems.
 *
 * @author yur
 */
public final class ParserErrorListener implements ANTLRErrorListener {

    private final ErrorListener listener;
    public boolean hasErrors = false;

    public ParserErrorListener(ErrorListener listener) {
        this.listener = listener;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1,
            String string, RecognitionException re) {
        hasErrors = true;
        listener.onError(new SyntaxError());
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1,
            boolean bln, BitSet bitset, ATNConfigSet atncs) {
        hasErrors = true;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i,
            int i1, BitSet bitset, ATNConfigSet atncs) {
        hasErrors = true;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1,
            int i2, ATNConfigSet atncs) {
        hasErrors = true;
        throw new UnsupportedOperationException("Not supported yet.");
    }
}