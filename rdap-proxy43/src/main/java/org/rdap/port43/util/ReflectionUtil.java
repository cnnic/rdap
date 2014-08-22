package org.rdap.port43.util;

/**
 * reflection util.
 * 
 * @author jiashuo
 * 
 */
public class ReflectionUtil {

    /**
     * create instance by class name.
     * 
     * @param className
     *            class name.
     * @return instance.
     */
    public static Object createInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
