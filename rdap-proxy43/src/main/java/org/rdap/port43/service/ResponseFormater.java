package org.rdap.port43.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ezvcard.Ezvcard;
import ezvcard.Ezvcard.ParserChainJsonString;
import ezvcard.VCard;

/**
 * 
 * @author jiashuo
 * 
 */
public class ResponseFormater {
    private static ThreadLocal<MutableInteger> depth =
            new ThreadLocal<MutableInteger>() {
                @Override
                protected MutableInteger initialValue() {
                    return new MutableInteger(0);
                }
            };
    private final static String tabs = StringUtils.repeat("\t", 20);

    public static String format(Map map) {
        if (null == map) {
            return StringUtils.EMPTY;
        }
        StringBuffer result = new StringBuffer(1024);
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            Object value = map.get(key);
            formatObject(key, value, result);
        }
        return result.toString();
    }

    public static void formatObject(String key, Object object,
            StringBuffer result) {
        if ("vcardArray".equals(key)) {
            // System.err.println("add "+getDepth()+" tab for key:"+key);
            result.append(tabs, 0, getDepth());
            String jcardString = JsonUtil.toJson(object);
            ParserChainJsonString e = Ezvcard.parseJson(jcardString);
            List<VCard> list = e.all();
            String displayValue = Ezvcard.write(list.get(0)).prodId(false).go();
            String replacement = StringUtils.substring(tabs, 0, getDepth() + 1);// add
                                                                                // tab
            displayValue = "\r\n" + displayValue;
            displayValue = StringUtils.removeEnd(displayValue, "\r\n");
            displayValue =
                    StringUtils.replace(displayValue, "\r\n", "\r\n"
                            + replacement);
            result.append(key);
            result.append(":");
            result.append("\r\n");
            result.append(displayValue);
            result.append("\r\n");
        } else if (object instanceof String) {
            // System.err.println("add "+getDepth()+" tab for key:"+key);
            result.append(tabs, 0, getDepth());
            result.append(key);
            result.append(":");
            result.append(object);
            result.append("\r\n");
        } else if (object instanceof Map) {
            // System.err.println("add "+getDepth()+" tab for key:"+key);
            result.append(tabs, 0, getDepth());
            result.append(key);
            result.append(":");
            result.append("\r\n");
            depth.get().increment();// map: add depth
            // System.err.println("inc for:"+key+"..."+object);
            // System.err.println(depth.get().get());
            formatMap((Map) object, result);
            depth.get().decrement();
        } else if (object instanceof List) {
            formatList(key, (List) object, result);
        }
    }

    private static int getDepth() {
        return depth.get().get();
    }

    public static void formatMap(Map<String, Object> map, StringBuffer result) {
        if (null == map) {
            return;
        }
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            Object value = map.get(key);
            formatObject(key, value, result);
        }
        return;
    }

    public static void formatList(String key, List<Object> list,
            StringBuffer result) {
        if (null == list) {
            return;
        }
        for (Object object : list) {
            formatObject(key, object, result);
        }
    }
}

class MutableInteger {
    private int value;

    MutableInteger(int value) {
        this.value = value;
    }

    public final int get() {
        return value;
    }

    public final void increment() {
        ++value;
    }

    public final void decrement() {
        --value;
    }
}
