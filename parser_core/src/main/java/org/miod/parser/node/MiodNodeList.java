/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

/**
 * A list of expressions, e.g. array literal or proc arguments enumeration.
 *
 * @author yur
 */
public final class MiodNodeList extends MiodNodeData {

    public final MiodNodeData[] list;

    public MiodNodeList(MiodNodeData[] list) {
        this.list = list;
    }
}
