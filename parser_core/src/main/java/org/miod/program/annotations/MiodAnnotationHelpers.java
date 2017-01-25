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

    public static String asStringValue(MiodNodeData data) {
        if (data instanceof MiodNodeValue) {
            if (((MiodNodeValue) data).value instanceof StringValue) {
                return ((StringValue) ((MiodNodeValue) data).value).value;
            }
        }
        return null;
    }

    public static MiodNodeData[] asNodeDataArray(MiodNodeData data) {
        if (data instanceof MiodNodeList) {
            return ((MiodNodeList) data).list;
        }
        return null;
    }

    private static CAttrAnnotation newCAttr(MiodNodeDict dict, ErrorListener lst,
            SymbolLocation loc) {

        String[] headers = null;
        String cname = null;
        
        if (dict != null) {
            String invalidKey = findInvalidKey(dict.map, CAttrAnnotation.VALID_KEYS);
            if (invalidKey != null) {
                lst.onError(new UnknownIdentifier(loc, invalidKey));
                return null;
            }

            final MiodNodeData cnameObj = dict.map.get("cname");
            if (cnameObj != null) {
                final String cnameValue = asStringValue(cnameObj);
                if (cnameValue != null) {
                    cname = cnameValue;
                } else {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }

            MiodNodeData headersObj = dict.map.get("headers");
            if (headersObj != null) {
                final MiodNodeData[] dataArray = asNodeDataArray(headersObj);
                if (dataArray != null) {
                    headers = new String[dataArray.length];
                    int index = 0;
                    for (MiodNodeData n : dataArray) {
                        final String value = asStringValue(n);
                        if (value != null) {
                            headers[index] = value;
                        } else {
                            lst.onError(new TypesMismatch(loc));
                            return null;
                        }
                        ++index;
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
