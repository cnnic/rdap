package org.rdap.port43.service.format;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.service.JsonUtil;

import ezvcard.Ezvcard;
import ezvcard.Ezvcard.ParserChainJsonString;
import ezvcard.VCard;

/**
 * 
 * @author jiashuo
 * 
 */
public class TextFormater implements Formater {
    /**
     * tabs for format.
     */
    private final static String TABS = StringUtils.repeat("\t", 20);

    @Override
    public String format(Map map) {
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

    private static ThreadLocal<MutableInteger> depth =
            new ThreadLocal<MutableInteger>() {
                @Override
                protected MutableInteger initialValue() {
                    return new MutableInteger(0);
                }
            };

    public void formatObject(String key, Object object, StringBuffer result) {
        if ("vcardArray".equals(key)) {
            // System.err.println("add "+getDepth()+" tab for key:"+key);
            result.append(TABS, 0, getDepth());
            String jcardString = JsonUtil.toJson(object);
            ParserChainJsonString e = Ezvcard.parseJson(jcardString);
            List<VCard> list = e.all();
            String displayValue = Ezvcard.write(list.get(0)).prodId(false).go();
            String replacement = StringUtils.substring(TABS, 0, getDepth() + 1);// add
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
            result.append(TABS, 0, getDepth());
            result.append(key);
            result.append(":");
            result.append(object);
            result.append("\r\n");
        } else if (object instanceof Map) {
            // System.err.println("add "+getDepth()+" tab for key:"+key);
            result.append(TABS, 0, getDepth());
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

    private int getDepth() {
        return depth.get().get();
    }

    public void formatMap(Map<String, Object> map, StringBuffer result) {
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

    public void formatList(String key, List<Object> list, StringBuffer result) {
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
