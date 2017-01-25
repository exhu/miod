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
import org.miod.program.values.ArrayValue;
import org.miod.program.values.MiodValue;
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

    @Deprecated
    public static MiodNodeData[] asNodeDataArray(MiodNodeData data) {
        if (data instanceof MiodNodeList) {
            return ((MiodNodeList) data).list;
        }
        return null;
    }

    @Deprecated
    public static String[] asStringArray(MiodNodeData data) {
        final MiodNodeData[] dataArray = asNodeDataArray(data);
        String[] strArray = null;
        if (dataArray != null) {
            strArray = new String[dataArray.length];
            int index = 0;
            for (MiodNodeData n : dataArray) {
                final String value = asStringValue(n);
                if (value != null) {
                    strArray[index] = value;
                } else {
                    return null;
                }
                ++index;
            }
        }
        return strArray;
    }

    public static String[] asStringArrayValue(MiodNodeData data) {
        String[] values = null;
        if (data instanceof MiodNodeValue) {
            MiodNodeValue v = (MiodNodeValue) data;
            if (v.value instanceof ArrayValue) {
                ArrayValue aV = (ArrayValue) v.value;
                values = new String[aV.values.length];
                int index = 0;
                for (MiodValue s : aV.values) {
                    if (s instanceof StringValue) {
                        values[index] = ((StringValue)s).value;
                        ++index;
                    } else {
                        return null;
                    }
                }
            }
        }
        return values;
    }

    private static CAttrAnnotation newCAttr(MiodNodeDict dict, ErrorListener lst,
            SymbolLocation loc) {

        String[] headers = null;
        String[] sysHeaders = null;
        String cname = null;

        if (dict != null) {
            String invalidKey = findInvalidKey(dict.map, CAttrAnnotation.VALID_KEYS);
            if (invalidKey != null) {
                lst.onError(new UnknownIdentifier(loc, invalidKey));
                return null;
            }

            final MiodNodeData cnameData = dict.map.get("cname");
            if (cnameData != null) {
                final String cnameValue = asStringValue(cnameData);
                if (cnameValue != null) {
                    cname = cnameValue;
                } else {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }

            final MiodNodeData headersData = dict.map.get("headers");
            if (headersData != null) {
                headers = asStringArrayValue(headersData);
                if (headers == null) {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }

            final MiodNodeData sysHeadersData = dict.map.get("sysHeaders");
            if (sysHeadersData != null) {
                sysHeaders = asStringArrayValue(sysHeadersData);
                if (sysHeaders == null) {
                    lst.onError(new TypesMismatch(loc));
                    return null;
                }
            }
        }

        return new CAttrAnnotation(cname, headers);
    }
}
