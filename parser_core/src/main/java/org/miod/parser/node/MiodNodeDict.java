/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.parser.node;

import java.util.Map;

/**
 *
 * @author yur
 */
public final class MiodNodeDict extends MiodNodeData {
    public final Map<String, MiodNodeData> map;
    public MiodNodeDict(Map<String, MiodNodeData> map) {
        this.map = map;
    }

}
