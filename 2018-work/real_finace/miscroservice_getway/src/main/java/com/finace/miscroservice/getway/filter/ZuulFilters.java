package com.finace.miscroservice.getway.filter;

import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.UidUtils;
import com.finace.miscroservice.getway.interpect.DataAnalysisInterceptor;
import com.finace.miscroservice.getway.interpect.IpBlackList;
import com.finace.miscroservice.getway.interpect.LocalRedirectInterceptor;
import com.finace.miscroservice.getway.interpect.OnceReqInterceptor;
import com.finace.miscroservice.getway.util.OptionsCheckUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.finace.miscroservice.commons.log.Log.SHORT_TRACE_ID;
import static com.finace.miscroservice.getway.filter.UserAuthFilter.getId;
import static com.finace.miscroservice.getway.interpect.AccessInterceptor.PASS;

@Component
public class ZuulFilters extends ZuulFilter {


    private Log logger = Log.getInstance(ZuulFilters.class);


    @Override
    public String filterType() {
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


    @Autowired
    private OnceReqInterceptor onceReqInterceptor;

    @Autowired
    private DataAnalysisInterceptor dataAnalysisInterceptor;


    @Autowired
    private LocalRedirectInterceptor localRedirectInterceptor;

    @Override
    public Object run() {

        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();

        //预检查
        if (OptionsCheckUtil.isContain(request)) {
            forbidForward(context);
            return null;

        }

        Map<String, String> allHeaders = context.getZuulRequestHeaders();

        //添加是否需要重定向的标识
        allHeaders.put(JwtToken.AUTH_SUFFIX, JwtToken.AUTH_SUFFIX);

        //转换request并且添加跟踪Id
        allHeaders.put(SHORT_TRACE_ID, Log.getTraceIdByConvertRequest(request));


        //直接略过uid检查
        if (request.getAttribute(PASS) != null) {
            return null;
        }

        String uid = request.getHeader(JwtToken.UID);

        //验证Uid是否符合规范
        if (StringUtils.isEmpty(uid) || !UidUtils.isRegularUidByDevice(uid, DeviceEnum.H5, DeviceEnum.ANDROID, DeviceEnum.IOS)) {

            logger.warn("Ip={} 方法={} uid={}不符合规范", Iptools.gainRealIp(request), request.getRequestURI(), uid);
            //设置黑名单
            IpBlackList.setBlackList(Iptools.gainRealIp(request));
            //禁止转发
            forbidForward(context);
            return null;
        }


        String id = getId();


        //设置Id
        if (!id.isEmpty()) {
            allHeaders.put(JwtToken.ID, id);
        }


        //once检查
        if (!onceReqInterceptor.isInvalid(request)) {
            //禁止转发
            forbidForward(context);
            return null;
        }


        //做数据统计转发
        //dataAnalysisInterceptor.forward(request, uid,id);


        //重定向转发
        if (localRedirectInterceptor.redirect(context)) {
            return null;
        }

        return null;
    }


    /**
     * 禁止转发
     *
     * @param context request上下文
     */
    private void forbidForward(RequestContext context) {
        context.setSendZuulResponse(false);
    }


    /**
     * 跨域解决
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
        config.addAllowedHeader(JwtToken.TOKEN);// 允许访问的头信息
        config.addAllowedHeader(JwtToken.UID);// 允许访问的头信息
        config.addExposedHeader(JwtToken.TOKEN);//允许暴露的头部信息
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);


    }


}
