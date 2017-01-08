/*
    Copyright 2016 Yury Benesh
    see COPYING.txt
 */
package org.miod.program.annotations;

import java.util.Map;
import java.util.Set;
import org.miod.parser.ErrorListener;
import org.miod.parser.node.MiodNodeData;
import org.miod.parser.node.MiodNodeDict;
import org.miod.parser.node.MiodNodeList;
import org.miod.parser.node.MiodNodeValue;
import org.miod.program.errors.TypesMismatch;
import org.miod.program.errors.UnknownIdentifier;
import org.miod.program.symbol_table.SymbolLocation;
import org.miod.program.values.StringValue;

/**
 *
 * @author yur
 */
public final class MiodAnnotationHelpers {

    private MiodAnnotationHelpers() {
    }

    static public boolean isBuiltin(String name) {
        return name.equals(CAttrAnnotation.NAME);
    }

    static public MiodBuiltinAnnotation newBuiltin(String name, MiodNodeDict dict,
            ErrorListener lst, SymbolLocation loc) {
        if (name.equals(CAttrAnnotation.NAME)) {
            return newCAttr(dict, lst, loc);
        }
        return null;
    }

    private static String findInvalidKey(Map<String, MiodNodeData> map, Set<String> validKeys) {
        for (String k : map.keySet()) {
            if (!validKeys.contains(k)) {
                return k;
            }
        }
        return null;
    }

    private static CAttrAnnotation newCAttr(MiodNodeDict dict, ErrorListener lst,
            SymbolLocation loc) {

        String[] headers = null;
        String cname = null;

        // TODO refactor the mess below:

        if (dict != null) {
            String invalidKey = findInvalidKey(dict.map, CAttrAnnotation.VALID_KEYS);
            if (invalidKey != null) {
                lst.onError(new UnknownIdentifier(loc, invalidKey));
                return null;
            }

            if (dict.map.get("cname") != null) {
                if (dict.map.get("cname") instanceof MiodNodeValue) {
                    if (((MiodNodeValue) dict.map.get("cname")).value instanceof StringValue) {
                        cname = ((StringValue)((MiodNodeValue) dict.map.get("cname")).value).value;
                    } else {
                        lst.onError(new TypesMismatch(loc));
                        return null;
                    }

                } else {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }

            if (dict.map.get("headers") != null) {
                if (dict.map.get("headers") instanceof MiodNodeList) {
                    MiodNodeList headersNodes = (MiodNodeList) dict.map.get("headers");
                    if (headersNodes.list[0] instanceof MiodNodeValue
                            && ((MiodNodeValue) headersNodes.list[0]).value instanceof StringValue) {
                        headers = new String[headersNodes.list.length];
                        int index = 0;
                        for (MiodNodeData n : headersNodes.list) {
                            MiodNodeValue valueNode = (MiodNodeValue) n;
                            headers[index] = ((StringValue) valueNode.value).value;
                            ++index;
                        }
                    } else {
                        lst.onError(new TypesMismatch(loc));
                        return null;
                    }
                } else {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }
        }

        return new CAttrAnnotation(cname, headers);
    }
}
