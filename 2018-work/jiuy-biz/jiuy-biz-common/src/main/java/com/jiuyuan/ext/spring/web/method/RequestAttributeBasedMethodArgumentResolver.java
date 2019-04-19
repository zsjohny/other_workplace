package com.jiuyuan.ext.spring.web.method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestAttributeBasedMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private Class<?>[] classes;

    private String attributeName;

    public RequestAttributeBasedMethodArgumentResolver(String attributeName, Class<?>... classes) {
        this.classes = classes;
        this.attributeName = attributeName;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        for (Class<?> clazz : classes) {
            if (paramType == clazz) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return request.getAttribute(attributeName);
    }
}
