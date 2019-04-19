package com.jiuyuan.ext.spring.convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.jiuyuan.util.enumeration.EnumCache;
import com.jiuyuan.util.enumeration.IntEnum;
import com.jiuyuan.util.enumeration.IntEnumCache;

public class StringToIntEnum<T extends Enum<T> & IntEnum> implements Converter<String, IntEnum> {

    private Class<T> enumType;

    public StringToIntEnum(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public IntEnum convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        source = source.trim();

        T value = EnumCache.getEnumValue(enumType, source);
        if (value == null) {
            int intValue = Integer.parseInt(source);
            value = IntEnumCache.getEnumValue(enumType, intValue);
        }

        return value;
    }
}
