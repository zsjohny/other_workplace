package com.jiuyuan.util.placeholder;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class StringPlaceholderResolver {

    public static final String DEFAULT_PREFIX = "${";

    public static final String DEFAULT_SUFFIX = "}";

    public static final String SHARP_PREFIX = "#{";

    public static final StringPlaceholderResolver INSTANCE = new StringPlaceholderResolver(DEFAULT_PREFIX,
        DEFAULT_SUFFIX);

    public static final StringPlaceholderResolver INSTANCE_SHARP = new StringPlaceholderResolver(SHARP_PREFIX,
        DEFAULT_SUFFIX);

    private PropertyPlaceholderHelper helper;

    public StringPlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
        this.helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix);
    }

    public String resolve(String template, Object object) {
        try {
            final Map<String, Object> map = PropertyUtils.describe(object);
            return resolve(template, map);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to resolve template: " + template);
        }
    }

    public String resolve(String template, final Map<String, ?> map) {
        try {
            return helper.replacePlaceholders(template, new PlaceholderResolver() {

                public String resolvePlaceholder(String placeholderName) {
                    Object obj = map.get(placeholderName);
                    return obj != null ? obj.toString() : "";
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Unable to resolve template: " + template, e);
        }
    }
}
