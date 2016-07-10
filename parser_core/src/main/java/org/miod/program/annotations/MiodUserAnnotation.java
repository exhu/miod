/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program.annotations;

import org.miod.program.SymItem;
import org.miod.program.values.MiodValue;

/**
 *
 * @author yur
 */
public final class MiodUserAnnotation extends MiodAnnotation {
    private final MiodValue value;
    private final SymItem struct;

    public MiodUserAnnotation(MiodValue value, SymItem struct) {
        super(struct.name);
        this.value = value;
        this.struct = struct;
    }

}
