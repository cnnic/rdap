package org.restfulwhois.rdap.bean;



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
     * get ObjectClassNameEnum by name.
     * 
     * @param name
     *           
     * @return objectClassNameEnum .
     */
    public static ObjectClassNameEnum getObjectClassNameEnum(String name) {
    	ObjectClassNameEnum[] objectClassNameEnums = ObjectClassNameEnum.values();
        for (ObjectClassNameEnum objectClassNameEnum : objectClassNameEnums) {
            if (objectClassNameEnum.getName().equals(name)) {
                return objectClassNameEnum;
            }
        }
        return null;
    }
    
    /**
     * get ObjectClassNameEnum name.
     * 
     * @return ObjectClassNameEnum name.
     */
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
