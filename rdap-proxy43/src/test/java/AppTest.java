

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.rdap.port43.util.JsonUtil;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public static void main(String[] args) {
        Domain domain = new Domain("cnnic.cn");
        List<Entity> entityList = new ArrayList<Entity>();
        entityList.add(new Entity("john", "1"));
        entityList.add(new Entity("merry", "2"));
        domain.setEntityList(entityList);
        domain.setEntity(new Entity("bob", "3"));
        
        String json =
                "{\"address\":[{\"a\":\"b\"},{\"c\":\"d\"}],\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
        Map convertToMap = JsonUtil.deserializateJsonToMap(json);
        System.err.println(ToStringBuilder.reflectionToString(convertToMap,
                RecursiveToStringStyle.getInstance()));
        
        
//        System.err.println(ToStringBuilder.reflectionToString(domain,
//                RecursiveToStringStyle.getInstance()));
        // new MultiLineToStringStyle()));
    }

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}

class MultiLineToStringStyle extends ToStringStyle {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructor.
     * </p>
     * 
     * <p>
     * Use the static constant rather than instantiating.
     * </p>
     */
    MultiLineToStringStyle() {
        super();
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
        this.setFieldNameValueSeparator(":");
        this.setContentStart(null);
        this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
        this.setFieldSeparatorAtStart(true);
        this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
    }

    /**
     * <p>
     * Ensure <code>Singleton</code> after serialization.
     * </p>
     * 
     * @return the singleton
     */
    private Object readResolve() {
        return ToStringStyle.MULTI_LINE_STYLE;
    }

}

class Domain {
    private String name;
    private Entity entity;
    private List<Entity> entityList;

    public Domain(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

}

class Entity {
    private String name;
    private String age;

    public Entity(String name, String age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

/**
 * recursive to string style.
 * 
 * @author jiashuo
 * 
 */
class RecursiveToStringStyle extends ToStringStyle {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    private static final RecursiveToStringStyle INSTANCE =
            new RecursiveToStringStyle(13);

    /**
     * get instance.
     * 
     * @return instance.
     */
    public static ToStringStyle getInstance() {
        return INSTANCE;
    }

    /**
     * to string.
     * 
     * @param value
     *            .
     * @return string.
     */
    public static String toString(Object value) {
        final StringBuffer sb = new StringBuffer(1024);
        INSTANCE.appendDetail(sb, null, value);
        return sb.toString();
    }

    /**
     * max depth.
     */
    private final int maxDepth;
    private final String tabs;

    private ThreadLocal<MutableInteger> depth =
            new ThreadLocal<MutableInteger>() {
                @Override
                protected MutableInteger initialValue() {
                    return new MutableInteger(0);
                }
            };

    protected RecursiveToStringStyle(int maxDepth) {
        this.maxDepth = maxDepth;
        tabs = StringUtils.repeat("\t", maxDepth);
        this.setArraySeparator(null);
        this.setUseClassName(false);
        this.setArrayStart(null);
        this.setFieldNameValueSeparator(":");
        this.setContentStart(null);
        this.setFieldSeparator(SystemUtils.LINE_SEPARATOR);
        this.setFieldSeparatorAtStart(false);
        setUseIdentityHashCode(false);
        // setFieldSeparator(SystemUtils.LINE_SEPARATOR);
        setContentEnd(null);
    }

    private int getDepth() {
        return depth.get().get();
    }

    private void padDepth(StringBuffer buffer) {
        buffer.append(tabs, 0, getDepth());
    }

    private StringBuffer appendTabified(StringBuffer buffer, String value) {
        // return buffer.append(String.valueOf(value).replace("\n", "\n" +
        // tabs.substring(0, getDepth())));
        Matcher matcher = Pattern.compile("\n").matcher(value);
        String replacement = "\n" + tabs.substring(0, getDepth());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer;
    }

    @Override
    protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
        super.appendFieldEnd(buffer, fieldName);
    }

    @Override
    protected void appendFieldSeparator(StringBuffer buffer) {
        buffer.append(getFieldSeparator());
//        padDepth(buffer);
        buffer.append(tabs, 0, getDepth()-1);
    }

    @Override
    public void appendStart(StringBuffer buffer, Object object) {
        depth.get().increment();
        super.appendStart(buffer, object);
    }

    @Override
    public void appendEnd(StringBuffer buffer, Object object) {
        super.appendEnd(buffer, object);
        buffer.setLength(buffer.length() - getContentEnd().length());
        buffer.append(SystemUtils.LINE_SEPARATOR);
        depth.get().decrement();
        padDepth(buffer);
        appendContentEnd(buffer);
    }

    @Override
    protected void removeLastFieldSeparator(StringBuffer buffer) {
        int len = buffer.length();
        int sepLen = getFieldSeparator().length() + getDepth();
        if (len > 0 && sepLen > 0 && len >= sepLen) {
            buffer.setLength(len - sepLen);
        }
    }

    private boolean noReflectionNeeded(Object value) {
        try {
            return value != null
                    && (value.getClass().getName().startsWith("java.lang.") || value
                            .getClass().getMethod("toString")
                            .getDeclaringClass() != Object.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName,
            Object value) {
        if (getDepth() >= maxDepth || noReflectionNeeded(value)) {
            appendTabified(buffer, String.valueOf(value));
        } else {
            buffer.append("\r\n");
            padDepth(buffer);
            new ReflectionToStringBuilder(value, this, buffer, null, false,
                    false).toString();
        }
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName,
            Collection coll) {
        buffer.append("\r\n");
        padDepth(buffer);
        for(Object obj:coll.toArray()){
            this.append(buffer, fieldName, obj, true);
        }
//        buffer.append(ReflectionToStringBuilder.toString(coll.toArray(), this,
//                true, true));
    }

    /**
     * Mutable Integer.
     * 
     * @author jiashuo
     * 
     */
    static class MutableInteger {
        private int value;

        MutableInteger(int value) {
            this.value = value;
        }

        public final int get() {
            return value;
        }

        public final void increment() {
            ++value;
        }

        public final void decrement() {
            --value;
        }
    }
}