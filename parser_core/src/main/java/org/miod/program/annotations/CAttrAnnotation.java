/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * "_cattr" annotation, all fields are optional.
 *
 * @author yur
 */
public final class CAttrAnnotation extends MiodBuiltinAnnotation {
    public static final String NAME = "_cattr";
    public static final HashSet<String> VALID_KEYS = new HashSet<>(
            Arrays.asList("cname", "headers", "sysHeaders"));

    public final String cname;
    public final String[] headers;

    public CAttrAnnotation(String cname, String[] headers) {
        super(NAME);
        this.cname = cname;
        this.headers = headers;
    }

}
