/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.rdap.port43.service.format;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.util.JsonUtil;

import ezvcard.Ezvcard;
import ezvcard.Ezvcard.ParserChainJsonString;
import ezvcard.VCard;

/**
 * format response to plain text.
 * 
 * <pre>
 * Format:
 *      nested values indent with '\t';
 *      key and value are separated by ':';
 *      VCARDs are displayed by text format;
 *      each item in array is displayed in a new line, with array name.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public class TextFormater implements Formater {

    /**
     * separator between key and value.
     */
    private static final String KEY_VALUE_SEPARATOR = ":";
    /**
     * line separator.
     */
    private static final String LINE_SEPARATOR = "\r\n";
    /**
     * formated result buffer initial size.
     */
    private static final int FORMATED_RESULT_BUFFER_INIT_SIZE = 1024;
    /**
     * max indent size.
     */
    private static final int MAX_INDENT_SIZE = 20;
    /**
     * tabs for format.
     */
    private final static String MAX_INDENTS = StringUtils.repeat("\t",
            MAX_INDENT_SIZE);

    @Override
    public String format(Map map) {
        if (null == map) {
            return StringUtils.EMPTY;
        }
        StringBuffer result =
                new StringBuffer(FORMATED_RESULT_BUFFER_INIT_SIZE);
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            Object value = map.get(key);
            formatObject(key, value, result);
        }
        return result.toString();
    }

    /**
     * used to maintain level of object.
     */
    private static ThreadLocal<MutableInteger> depth =
            new ThreadLocal<MutableInteger>() {
                @Override
                protected MutableInteger initialValue() {
                    return new MutableInteger(0);
                }
            };

    /**
     * format object.
     * 
     * @param key
     *            key.
     * @param object
     *            object.
     * @param result
     *            result.
     */
    public void formatObject(String key, Object object, StringBuffer result) {
        if ("vcardArray".equals(key)) {
            result.append(MAX_INDENTS, 0, getDepth());
            String displayValue = getTextFormatedVcardStr(object);
            String replacement =
                    StringUtils.substring(MAX_INDENTS, 0, getDepth() + 1);// add
            displayValue = LINE_SEPARATOR + displayValue;
            displayValue = StringUtils.removeEnd(displayValue, LINE_SEPARATOR);
            displayValue =
                    StringUtils.replace(displayValue, LINE_SEPARATOR,
                            LINE_SEPARATOR + replacement);
            result.append(key);
            result.append(KEY_VALUE_SEPARATOR);
            result.append(displayValue);
            result.append(LINE_SEPARATOR);
        } else if (object instanceof String) {
            result.append(MAX_INDENTS, 0, getDepth());
            result.append(key);
            result.append(KEY_VALUE_SEPARATOR);
            result.append(object);
            result.append(LINE_SEPARATOR);
        } else if (object instanceof Map) {
            result.append(MAX_INDENTS, 0, getDepth());
            result.append(key);
            result.append(KEY_VALUE_SEPARATOR);
            result.append(LINE_SEPARATOR);
            depth.get().increment();// map: add depth
            formatMap((Map) object, result);
            depth.get().decrement();
        } else if (object instanceof List) {
            formatList(key, (List) object, result);
        }
    }

    /**
     * get text formated VCARD string.
     * 
     * @param object
     *            object.
     * @return string.
     */
    private String getTextFormatedVcardStr(Object object) {
        String jcardString = JsonUtil.toJson(object);
        ParserChainJsonString e = Ezvcard.parseJson(jcardString);
        List<VCard> list = e.all();
        String displayValue = Ezvcard.write(list.get(0)).prodId(false).go();
        return displayValue;
    }

    /**
     * get depth.
     * 
     * @return depth.
     */
    private int getDepth() {
        return depth.get().get();
    }

    /**
     * format map.
     * 
     * @param map
     *            map.
     * @param result
     *            result.
     */
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

    /**
     * format list.
     * 
     * @param key
     *            key.
     * @param list
     *            list.
     * @param result
     *            result.
     */
    public void formatList(String key, List<Object> list, StringBuffer result) {
        if (null == list) {
            return;
        }
        for (Object object : list) {
            formatObject(key, object, result);
        }
    }

}

/**
 * MutableInteger.
 * 
 * @author jiashuo
 * 
 */
class MutableInteger {
    /**
     * int val.
     */
    private int value;

    /**
     * default constructor.
     * 
     * @param value
     *            value.
     */
    MutableInteger(int value) {
        this.value = value;
    }

    /**
     * get value.
     * 
     * @return value
     */
    public final int get() {
        return value;
    }

    /**
     * increment value.
     */
    public final void increment() {
        ++value;
    }

    /**
     * decrement value.
     */
    public final void decrement() {
        --value;
    }
}
