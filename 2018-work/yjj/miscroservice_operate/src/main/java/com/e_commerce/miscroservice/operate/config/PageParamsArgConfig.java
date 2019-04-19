package com.e_commerce.miscroservice.operate.config;

import com.e_commerce.miscroservice.commons.utils.BeanKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/14 17:17
 * @Copyright 玖远网络
 */
@Configuration
public class PageParamsArgConfig extends WebMvcConfigurerAdapter{

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_NUM = "pageNum";
    private static final String PAGE_SIZE = "pageSize";
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add (new PageParamsArgResolver ());
    }


    /**
     * 老的前端页面分页跟新系统分页的兼容, 把 offset,limit转换成新框架的 pageSize,pageNum,pageNumber
     *
     * @author Charlie
     * @date 2018/11/14 18:39
     */
    public class PageParamsArgResolver implements HandlerMethodArgumentResolver{
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            String name = parameter.getParameterName ();
            if (StringUtils.isNotBlank (name)) {
                switch (name) {
                    case PAGE_NUMBER:
                    case PAGE_NUM:
                    case PAGE_SIZE:
                        return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }


        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            String name = parameter.getParameterName ();
            String val = webRequest.getParameter (name);
            if (StringUtils.isNotBlank (val)) {
                //如果传了
                return Integer.parseInt (val);
            }


            //如果没传,试着从offset,limit拿
            String offsetStr;
            String limitStr;
            if (BeanKit.hasNull ((offsetStr = webRequest.getParameter (OFFSET)), (limitStr = webRequest.getParameter (LIMIT)))) {
                //没有拿到
                return null;
            }


            //拿到了
            int offset = Integer.parseInt (offsetStr);
            int limit = Integer.parseInt (limitStr);

            //返回,并放到request对象中,支持{@code com.e_commerce.miscroservice.commons.annotation.service.Consume}
            switch (name) {
                case PAGE_SIZE:
                    webRequest.setAttribute (PAGE_SIZE, limit, SCOPE_REQUEST);
                    return limit;
                case PAGE_NUMBER:
                case PAGE_NUM:
                    int pageN = offset / limit + 1;
                    webRequest.setAttribute (PAGE_NUMBER, pageN, SCOPE_REQUEST);
                    webRequest.setAttribute (PAGE_NUM, pageN, SCOPE_REQUEST);
                    return pageN;
                default:
                    return null;
            }
        }

    }
}
