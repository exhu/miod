/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;

/**
 * "_cattr" annotation, all fields are optional.
 *
 * @author yur
 */
public final class CAttrAnnotation extends MiodBuiltinAnnotation {
    public static final String NAME = "_cattr";

    public final String cname;
    public final String[] headers;

    public CAttrAnnotation(String cname, String[] headers) {
        super(NAME);
        this.cname = cname;
        this.headers = headers;
    }

}
