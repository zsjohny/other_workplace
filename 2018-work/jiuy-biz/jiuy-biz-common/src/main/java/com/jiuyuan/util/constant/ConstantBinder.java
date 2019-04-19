package com.jiuyuan.util.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.SimpleTypeConverter;

import com.jiuyuan.util.constant.ArgCount;

/**
 * <pre>
 * 设置一个常量类的所有public static 非final字段的值。值的来源是这个常量类目录下的同名properties文件。
 * 字段名称和properties文件中key的映射规则见{@link #getMessageKey}。
 * 
 * properties文件支持占位符的嵌套和递归，并且在properties文件未定义相应的key时，从system properties和env中查找。
 * 如果仍然找不到，会抛出异常。
 * 
 * 不需绑定到properties文件的字段，可以定义为public static final ...
 * </pre>
 * 
 */
public class ConstantBinder {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /** 支持占位符嵌套和递归 */
    private static SpringPropertyPlaceholderConfigurer configurer = new SpringPropertyPlaceholderConfigurer();

    /** 基本数据类型转换 */
    private static SimpleTypeConverter converter = new SimpleTypeConverter();

    /** 用于识别MessageFormat pattern中的argument index */
    private static Pattern messageArgPattern = Pattern.compile("\\{(\\d+)(,[^}]+)?\\}", Pattern.DOTALL);

    /**
     * @param messageClass Class instance to which the properties are binded
     * @param charset Charset of properties files
     * @param classpathLocations Classpath locations of properties files. The latter properties file will overwrite
     *        properties of the same name which are defined by the former properties files. If this parameter is null,
     *        then the properties file is supposed to be located in the same directory as the message class.
     */
    public static void bind(Class<?> messageClass, String charset, String... classpathLocations) {
        bind(messageClass, loadProperties(messageClass, charset, classpathLocations));
    }

    public static void bind(Class<?> messageClass, Properties props) {
        try {
            Field[] fields = messageClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    int modifiers = field.getModifiers();
                    if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                        String key = getMessageKey(field);
                        String strVal = configurer.getValue(key, props);
                        if (MessageHolder.class == field.getType()) {
                            int expectedCount = getExpectedArgCount(field, messageClass);
                            int realCount = getRealArgCount(strVal);

                            if (realCount != expectedCount) {
                                throw new IllegalStateException("Argument count mismatch, expected: " + expectedCount +
                                    ", found: " + realCount);
                            }

                            field.set(null, new MessageHolder(strVal, expectedCount));
                        } else if (PlaceHolder.class == field.getType()) {
                            field.set(null, new PlaceHolder(strVal));
                        } else {
                            field.set(null, converter.convertIfNecessary(strVal, field.getType()));
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Error occurs when setting field: " + field.getName(), e);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error occurs when binding message class: " + messageClass.getName(), e);
        }
    }

    /**
     * @param messageClass Class instance to which the properties are binded
     * @param charset Charset of properties files
     * @param classpathLocations Classpath locations of properties files. The latter properties file will overwrite
     *        properties of the same name which are defined by the former properties files. If this parameter is null,
     *        then the properties file is supposed to be located in the same directory as the message class.
     */
    public static <V> Map<String, V> bind(Class<? extends Enum<?>> enumClass, Class<V> valueType, String charset,
                                          String... classpathLocations) {
        return bind(enumClass, valueType, loadProperties(enumClass, charset, classpathLocations));
    }

    public static <V> Map<String, V> bind(Class<? extends Enum<?>> enumClass, Class<V> valueType, Properties props) {
        try {
            Map<String, V> map = new HashMap<String, V>();

            Field[] fields = enumClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.getType() == enumClass) {
                        String key = getMessageKey(field);
                        String strVal = configurer.getValue(key, props);

                        if (MessageHolder.class == valueType) {
                            int expectedCount = getExpectedArgCount(field, enumClass);
                            int realCount = getRealArgCount(strVal);

                            if (realCount != expectedCount) {
                                throw new IllegalStateException("Argument count mismatch, expected: " + expectedCount +
                                    ", found: " + realCount);
                            }

                            @SuppressWarnings("unchecked")
                            V value = (V) new MessageHolder(strVal, expectedCount);
                            map.put(field.getName(), value);
                        } else if (PlaceHolder.class == valueType) {
                            @SuppressWarnings("unchecked")
                            V value = (V) new PlaceHolder(strVal);
                            map.put(field.getName(), value);
                        } else {
                            V value = converter.convertIfNecessary(strVal, valueType);
                            map.put(field.getName(), value);
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Error occurs when setting field: " + field.getName(), e);
                }
            }

            return map;
        } catch (Exception e) {
            throw new IllegalStateException("Error occurs when binding message class: " + enumClass.getName(), e);
        }
    }

    /**
     * <pre>
     * 从字段推断出properties文件里对应的key。如果字段有MessageBinderKey注解，那么使用注解的value()方法的返回值。否则
     * 使用_对field name进行分割，再以“.”连接，最后全部转换为小写。
     * 
     * 举例：
     * VPNConfig --> vpn.config
     * VPN_CONFIG --> vpn.config
     * </pre>
     * 
     * @param field 字段
     * @return properties文件里对应的key
     */
    private static String getMessageKey(Field field) {
        ConstantBinderKey binderKey = field.getAnnotation(ConstantBinderKey.class);
        if (binderKey != null) {
            return binderKey.value();
        } else {
            String[] parts = StringUtils.split(field.getName(), "_");
            return StringUtils.join(parts, '.').replace("._.", ".").toLowerCase();
        }
    }

    private static int getExpectedArgCount(Field field, Class<?> messageClass) {
        ArgCount argCount = field.getAnnotation(ArgCount.class);
        if (argCount == null) {
            argCount = messageClass.getAnnotation(ArgCount.class);
            if (argCount == null) {
                throw new IllegalStateException("Annotation @ArgCount is missing.");
            }
        }
        return argCount.value();
    }

    private static int getRealArgCount(String pattern) {
        int realCount = 0;

        Matcher matcher = messageArgPattern.matcher(pattern);
        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            realCount = Math.max(realCount, index + 1);
        }

        return realCount;
    }

    /**
     * @param messageClass Class instance to which the properties are binded
     * @param charset Charset of properties files
     * @param classpathLocations Classpath locations of properties files. The latter properties file will overwrite
     *        properties of the same name which are defined by the former properties files. If this parameter is null,
     *        then the properties file is supposed to be located in the same directory as the message class.
     */
    private static Properties loadProperties(Class<?> messageClass, String charset, String... classpathLocations) {
        Properties props = new Properties();
        if (classpathLocations == null || classpathLocations.length == 0) {
            props = PropertiesLoader.loadClasspathProperties(messageClass, charset);
        } else {
            for (String classpathLocation : classpathLocations) {
                props.putAll(PropertiesLoader.loadClasspathProperties(messageClass, classpathLocation, charset));
            }
        }
        return props;
    }
}
