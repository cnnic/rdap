package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * EntityTelephoneDto.
 * 
 * @author jiashuo.
 * 
 */
public class EntityTelephoneDto extends BaseDto {
    /**
     * pref.
     */
    private int pref;
    /**
     * types.
     */
    private String types;
    /**
     * number.
     */
    private String number;
    /**
     * extNumber.
     */
    private String extNumber;

    /**
     * get pref.
     * 
     * @return pref.
     */
    public int getPref() {
        return pref;
    }

    /**
     * set pref.
     * 
     * @param pref
     *            pref.
     */
    public void setPref(int pref) {
        this.pref = pref;
    }

    /**
     * get types.
     * 
     * @return types.
     */
    public String getTypes() {
        return types;
    }

    /**
     * set types.
     * 
     * @param types
     *            types.
     */
    public void setTypes(String types) {
        this.types = types;
    }

    /**
     * get number.
     * 
     * @return number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * set number.
     * 
     * @param number
     *            number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * get extNumber.
     * 
     * @return extNumber.
     */
    public String getExtNumber() {
        return extNumber;
    }

    /**
     * set extNumber.
     * 
     * @param extNumber
     *            extNumber.
     */
    public void setExtNumber(String extNumber) {
        this.extNumber = extNumber;
    }
}
