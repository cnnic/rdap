/**
 * 
 */
package cn.cnnic.rdap.bean;

import java.util.Map;
import java.util.Set;
import java.util.List;
import cn.cnnic.rdap.bean.Policy;

/**
 * singleton of the hidden fields.
 * 
 * @author weijunkai
 * 
 */
public final class PolicyFields {
    /**
     * the singleton instance.
     */
    static private PolicyFields instance = null;
    /**
     * the map of object and its columns.
     */
    Map<String, Set<String>> mapObjField;
    /**
     * the list of object and its columns.
     */
    List<Policy> listObjField;

    /**
     * constructor private.
     */
    private PolicyFields() {
    }

    /**
     * get singleton instance.
     */
    public static synchronized PolicyFields getInstance() {
        if (null == instance) {
            instance = new PolicyFields();
        }
        return instance;
    }

    /**
     * set the map about the objects and its columns.
     * 
     * @param mapObjField
     *            the map to set.
     */
    public void setMapObjField(Map<String, Set<String>> mapObjField) {
        this.mapObjField = mapObjField;
    }

    /**
     * get map about the objects and its columns.
     * 
     * @return map about the columns.
     */
    public Map<String, Set<String>> getMapObjField() {
        return mapObjField;
    }

    /**
     * get list about the objects and its columns.
     * 
     * @return list about the columns.
     */
    public List<Policy> getListObjField() {
        return listObjField;
    }

    /**
     * set the list about the objects and its columns.
     * 
     * @param listObjField
     *            list policy to set.
     */
    public void setListObjField(List<Policy> listPolicy) {
        this.listObjField = listPolicy;
    }
}
