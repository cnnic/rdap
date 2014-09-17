package org.restfulwhois.rdap.bean;

import com.fasterxml.jackson.annotation.JsonValue;



/**
 * ObjectClassNameEnum.
 * 
 * @author zhanyq
 * 
 */
public enum ObjectClassNameEnum {
	/**
     * 5 main object Class Name.
     */
    DOMAIN("domain"), ENTITY("entity"), NAMESERVER("nameserver"), AUTNUM(
            "autnum"), IP("ip network");
    
    /**
     * name of ObjectClassNameEnum.
     */
    private String name;

    /**
     * constructor.
     * 
     * @param name.
     *            
     */
    private ObjectClassNameEnum(String name) {
        this.name = name;
    }   
    
    /**
     * get ObjectClassNameEnum name.
     * 
     * @return ObjectClassNameEnum name.
     */
    @JsonValue
    public String getName() {
        return name;
    }

    /**
     * set ObjectClassNameEnum name.
     * 
     * @param name
     *            ObjectClassNameEnum name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
