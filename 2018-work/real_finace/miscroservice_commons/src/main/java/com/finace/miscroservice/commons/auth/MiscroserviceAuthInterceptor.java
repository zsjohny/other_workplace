package com.finace.miscroservice.commons.auth;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Rc4Utils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import feign.RequestInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.finace.miscroservice.commons.log.Log.SHORT_TRACE_ID;
import static com.finace.miscroservice.commons.utils.AopUtil.getReturnType;

/**
 * 服务与服务间传递的认证拦截
 */
@Configuration
@Aspect
@ConditionalOnExpression("${service.auth.enabled}")
public class MiscroserviceAuthInterceptor {


    private final String TOKEN = "token";
    private final long DEFAULT_ACCESS_TIME_OUT = 1000 * 60L;
    private Log log = Log.getInstance(MiscroserviceAuthInterceptor.class);

    /**
     * 微服务访问前拦截
     *
     * @return
     */
    @Bean
    public RequestInterceptor createMiscroserviceInterceptor() {
        return template ->
        {

            //添加认证
            template.header(TOKEN, Rc4Utils.toHexString(System.currentTimeMillis() + "", template.url()));
            //转换request并且添加跟踪Id
            RequestAttributes currentRequestContext = RequestContextHolder.getRequestAttributes();
            if (currentRequestContext != null) {
                template.header(SHORT_TRACE_ID, Log.getTraceIdByConvertRequest(((ServletRequestAttributes) currentRequestContext).getRequest()));

            }
        };

    }

    @Autowired
    private HttpServletRequest request;


    /**
     * 拦截条件 第一个是方法 第二个是类拦截
     */
    @Pointcut("(execution(@com.finace.miscroservice.commons.annotation.Auth * *(..))) || (within(@com.finace.miscroservice.commons.annotation.Auth *))")
    public void authInterceptor() {

    }


    /**
     * 微服务具体调用拦截
     *
     * @param joinPoint
     * @return
     */
    @Around("authInterceptor()")
    public Object miscroserviceInsideInvokingInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {

        String token = request.getHeader(TOKEN);
        if (StringUtils.isEmpty(token)) {
            String errorEcho = String.format("授权服务访问接口=%s,没有认证 访问失败", request.getRequestURI());
            log.warn(errorEcho);
            return getReturnType(joinPoint);
        }
        String accessTimeStr = Rc4Utils.toString(token, request.getRequestURI());

        if (StringUtils.isEmpty(accessTimeStr)) {
            String errorEcho = String.format("授权服务访问接口=%s,认证失败 ", request.getRequestURI());
            log.warn(errorEcho);
            return getReturnType(joinPoint);
        }
        if (System.currentTimeMillis() - Long.parseLong(accessTimeStr) > DEFAULT_ACCESS_TIME_OUT) {
            String errorEcho = String.format("授权服务访问接口=%s,认证超时 ", request.getRequestURI());
            log.warn(errorEcho);
            return getReturnType(joinPoint);
        }

        return joinPoint.proceed();

    }


}
