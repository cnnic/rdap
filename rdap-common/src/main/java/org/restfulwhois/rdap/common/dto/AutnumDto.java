package org.restfulwhois.rdap.common.dto;

/**
 * AutnumDto.
 * 
 * @author jiashuo.
 * 
 */
public class AutnumDto extends BaseDto {
    /**
     * startAutnum.
     */
    private Long startAutnum;
    /**
     * endAutnum.
     */
    private Long endAutnum;
    /**
     * name.
     */
    private String name;
    /**
     * type.
     */
    private String type;
    /**
     * country.
     */
    private String country;

    /**
     * get startAutnum.
     * 
     * @return startAutnum.
     */
    public Long getStartAutnum() {
        return startAutnum;
    }

    /**
     * set startAutnum.
     * 
     * @param startAutnum
     *            startAutnum.
     */
    public void setStartAutnum(Long startAutnum) {
        this.startAutnum = startAutnum;
    }

    /**
     * get endAutnum.
     * 
     * @return endAutnum.
     */
    public Long getEndAutnum() {
        return endAutnum;
    }

    /**
     * set endAutnum.
     * 
     * @param endAutnum
     *            endAutnum.
     */
    public void setEndAutnum(Long endAutnum) {
        this.endAutnum = endAutnum;
    }

    /**
     * get name.
     * 
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * set name.
     * 
     * @param name
     *            name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get country.
     * 
     * @return country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * set country.
     * 
     * @param country
     *            country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

}
