/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yur
 */
public final class EnumType extends StandardType {
    final Map<String, Integer> values = new HashMap<>();
}
