package com.e_commerce.miscroservice.getway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Configuration
public class TestA extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new Owner());
        super.addArgumentResolvers(argumentResolvers);

    }


    private class Owner implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            System.out.println(parameter.getParameterName());
            return true;
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


            System.out.println(parameter.getParameterName());
            mavContainer.addAttribute("");
            webRequest.setAttribute("test", webRequest.getParameterValues(parameter.getParameterName()), SCOPE_REQUEST);
            System.out.println(parameter.getParameterName());

            return null;
        }
    }

}
