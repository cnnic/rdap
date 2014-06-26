package cn.cnnic.rdap.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ReflectionUtils;

import cn.cnnic.rdap.bean.Policy;
import cn.cnnic.rdap.bean.HiddenColumn;

/**
 * check if the column is hidden.
 * 
 * @author weijunkai
 */
public final class HiddenColumnUtil {
    /**
     * constructor.
     */
    private HiddenColumnUtil() {
    }

    /**
     * check if the column is hidden by map.
     * 
     * @param strObj
     *            the string of object.
     * @param model
     *            the model of object.
     */
    public static void setHiddenColumnNull(final Object model,
            final String strObj) {
        HiddenColumn hidCol = HiddenColumn.getInstance();
        if (hidCol == null) {
            return;
        }
        Map<String, Set<String>> mapObjField = hidCol.getMapObjField();
        if (null == mapObjField) {
            return;
        }
        final int sizeMap = mapObjField.size();
        for (int i = 0; i < sizeMap; ++i) {
            if (!mapObjField.containsKey(strObj)) {
                return;
            }
            Set<String> setColumns = mapObjField.get(strObj);
            if (setColumns == null) {
                return;
            }
            Iterator<String> iterator = setColumns.iterator();
            while (iterator.hasNext() && model != null) {
                String strField = (String) iterator.next();
                try {
                    PropertyUtils.setSimpleProperty(model, strField, null);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    /**
     * set the map about the objects and its columns.
     * 
     * @param mapObjField
     *            the map to set.
     */
    public static void setMapPolicy(Map<String, Set<String>> mapObjField) {
        HiddenColumn hideCol = HiddenColumn.getInstance();
        if (hideCol == null) {
            return;
        }
        hideCol.setMapObjField(mapObjField);
    }

    /**
     * check if the column is hidden by list.
     * 
     * @param strObj
     *            the string of object.
     * @param model
     *            the model of object.
     */
    public static void setListHiddenColumnNull(Object model, String strObj) {
        HiddenColumn hidCol = HiddenColumn.getInstance();
        if (hidCol == null || model == null) {
            return;
        }
        List<Policy> listObjField = hidCol.getListObjField();
        if (null == listObjField) {
            return;
        }
        final int size = listObjField.size();
        for (int i = 0; i < size; ++i) {
            Policy policy = listObjField.get(i);
            if (0 == strObj.compareTo(policy.getModelType())) {
                String strField = (String) policy.getHideColumn();
                Object objModel = null;
                if (model != null) {
                    objModel = model;
                }

                try {
                    Method[] allMethods = ReflectionUtils
                            .getUniqueDeclaredMethods(objModel.getClass());
                    for (Method mthd : allMethods) {
                        String strMethod = mthd.getName();
                        final String strSet = "set";
                        final int posAfterSet = strSet.length();
                        String strMethodField = strMethod
                                .substring(posAfterSet);
                        if (strMethodField.compareToIgnoreCase(strField) == 0
                                && strMethod.startsWith("s")) {
                            PropertyUtils.setProperty(objModel, strField, null);
                        }
                    }
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    /**
     * set the list about the objects and its columns.
     * 
     * @param listObjField
     *            the list to set.
     */
    public static void setListPolicy(List<Policy> listObjField) {
        HiddenColumn hideCol = HiddenColumn.getInstance();
        if (hideCol == null) {
            return;
        }
        hideCol.setListObjField(listObjField);
    }
}
