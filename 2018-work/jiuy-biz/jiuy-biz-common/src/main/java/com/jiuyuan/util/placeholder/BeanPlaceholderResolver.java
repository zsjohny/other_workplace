package com.jiuyuan.util.placeholder;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class BeanPlaceholderResolver {

    public static final String DEFAULT_PREFIX = "${";

    public static final String DEFAULT_SUFFIX = "}";

    public static final String SHARP_PREFIX = "#{";

    public static final BeanPlaceholderResolver INSTANCE = new BeanPlaceholderResolver(DEFAULT_PREFIX, DEFAULT_SUFFIX);

    public static final BeanPlaceholderResolver INSTANCE_SHARP = new BeanPlaceholderResolver(SHARP_PREFIX,
        DEFAULT_SUFFIX);

    private PropertyPlaceholderHelper helper;

    public BeanPlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
        this.helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix);
    }

    public String resolve(String template, final Object object) {
        try {
            return helper.replacePlaceholders(template, new PlaceholderResolver() {

                public String resolvePlaceholder(String placeholderName) {
                    try {
                        Object obj = PropertyUtils.getProperty(object, placeholderName);
                        return obj != null ? obj.toString() : "";
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Unable to resolve template: " + template, e);
        }
    }
}
