package com.jiuyuan.ext.spring.convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToEnum<T extends Enum<T>> implements Converter<String, T> {

    private final Class<T> enumType;

    public StringToEnum(Class<T> enumType) {
        this.enumType = enumType;
    }

    public T convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        return Enum.valueOf(this.enumType, source.trim());
    }
}