package com.jiuyuan.ext.spring.web.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.jiuyuan.web.help.JsonResponse;

public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!JsonResponse.class.isAssignableFrom(clazz)) {
            return false;
        }
        return super.canWrite(clazz, mediaType);
    }
}
