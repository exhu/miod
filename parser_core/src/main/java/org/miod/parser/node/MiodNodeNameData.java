/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

/**
 *
 * @author yur
 */
public final class MiodNodeNameData extends MiodNodeData {
    public final String name;
    public final MiodNodeData data;
    public MiodNodeNameData(String name, MiodNodeData data) {
        this.name = name;
        this.data = data;
    }
}
