package com.spring_getway.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class ZuulFilters extends ZuulFilter {
    @Override
    public String filterType() {
//        return "error";
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();

        String name = request.getParameter("name");


        System.out.println("name ;" + name + ": having  coming");


        if (!"admin".equals(name)) {
            try {
                //关闭zuul的response 不然流关闭 后面书写不了
                context.setSendZuulResponse(false);
                context.getResponse().getWriter().write("sorry forbbin");
            } catch (IOException e) {

            }
        }


        return null;
    }
}
