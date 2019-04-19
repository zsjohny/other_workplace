package com.jiuyuan.ext.spring.convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.jiuyuan.util.enumeration.EnumCache;
import com.jiuyuan.util.enumeration.StringEnum;
import com.jiuyuan.util.enumeration.StringEnumCache;

public class StringToStringEnum<T extends Enum<T> & StringEnum> implements Converter<String, StringEnum> {

    private Class<T> enumType;

    public StringToStringEnum(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public StringEnum convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        source = source.trim();

        T value = EnumCache.getEnumValue(enumType, source);
        if (value == null) {
            value = StringEnumCache.getEnumValue(enumType, source);
        }

        return value;
    }
}
