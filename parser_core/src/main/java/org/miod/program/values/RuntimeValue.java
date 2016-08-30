/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.miod.program.values;

import org.miod.program.types.MiodType;

/** Unknown value e.g. for function calls, variables etc.
 *
 * @author yur
 */
public final class RuntimeValue extends MiodValue {
    public RuntimeValue(MiodType type) {
        super(type);
    }

}
