package org.restfulwhois.rdap.client.util;

/**
 * Supply methods to handle string
 * @author M.D.
 *
 */
public final class StringUtil{
    
    /**
     * Default constructor
     */
    private StringUtil(){}
    
    /**
     * Return true if the parameter s is empty or null
     * @param s String s
     * @return boolean
     */
    public static boolean isEmpty(String s){
        if (s == null || "".equals(s.trim())){
            return true;
        }
        return false;
    }
}