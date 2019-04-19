package com.jiuyuan.ext.spring.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.jiuyuan.util.enumeration.IntEnum;
import com.jiuyuan.util.enumeration.StringEnum;


public class CustomStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        Class<?>[] interfaces = targetType.getInterfaces();
        // 按接口声明的顺序查找
        for (Class<?> clazz : interfaces) {
            if (IntEnum.class.isAssignableFrom(clazz)) {
                return new StringToIntEnum(targetType);
            } else if (StringEnum.class.isAssignableFrom(clazz)) {
                return new StringToStringEnum(targetType);
            }
        }
        return new StringToEnum(targetType);
    }
}
