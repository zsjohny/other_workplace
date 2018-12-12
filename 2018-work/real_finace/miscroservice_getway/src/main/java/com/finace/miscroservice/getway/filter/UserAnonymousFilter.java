package com.finace.miscroservice.getway.filter;

import com.finace.miscroservice.getway.interpect.AccessInterceptor;
import com.finace.miscroservice.getway.util.OptionsCheckUtil;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 无需授权的过滤器
 */
public class UserAnonymousFilter extends AnonymousFilter {

    private AccessInterceptor accessInterceptor;

    public UserAnonymousFilter(AccessInterceptor accessInterceptor) {
        this.accessInterceptor = accessInterceptor;
    }


    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse req, Object mappedValue) {

        if (OptionsCheckUtil.isContain((HttpServletRequest) request)) {
            return super.onPreHandle(request, req, mappedValue);
        }

        //校验
        if (accessInterceptor.isPass(WebUtils.toHttp(request))) {
            return super.onPreHandle(request, req, mappedValue);
        }


        return false;


    }
}