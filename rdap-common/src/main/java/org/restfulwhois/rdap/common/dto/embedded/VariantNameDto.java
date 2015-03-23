package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * VariantNameDto.
 * 
 * @author jiashuo.
 * 
 */
public class VariantNameDto extends BaseDto {
    /**
     * ldhName.
     */
    private String ldhName;
    /**
     * unicodeName.
     */
    private String unicodeName;

    /**
     * get ldhName.
     * 
     * @return ldhName.
     */
    public String getLdhName() {
        return ldhName;
    }

    /**
     * set ldhName.
     * 
     * @param ldhName
     *            ldhName.
     */
    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    /**
     * get unicodeName.
     * 
     * @return unicodeName.
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * set unicodeName.
     * 
     * @param unicodeName
     *            unicodeName.
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

}
