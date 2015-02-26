package org.restfulwhois.rdap.client.util;

public class StringUtil{
    
    public static boolean isEmpty(String s){
        if (s == null || "".equals(s.trim())){
            return true;
        }else{
            return false;
        }
    }
}