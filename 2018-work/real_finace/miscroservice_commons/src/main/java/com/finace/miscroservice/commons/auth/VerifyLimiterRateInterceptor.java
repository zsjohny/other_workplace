package com.finace.miscroservice.commons.auth;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.annotation.RateVerify;
import com.finace.miscroservice.commons.config.RedisTemplateConfig;
import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.enums.RedisKeyEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UidUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.EncryptPubEnum.getKeyByFiled;
import static com.finace.miscroservice.commons.enums.InterceptorModeEnum.*;
import static com.finace.miscroservice.commons.utils.AopUtil.getReturnType;

/**
 * 验证频率拦截
 */
@Configuration
@Aspect
@ConditionalOnExpression("${verityLimeter.enabled}")
public class VerifyLimiterRateInterceptor extends RedisTemplateConfig {
    @Bean(name = "codeStrRedisTemplate")
    public ValueOperations<String, String> createCodeStrRedisTemplate() {
        return createTemplateCache(OperateEnum.STR).opsForValue();
    }

    @Bean(name = "limiterHashRedisTemplate")
    public HashOperations<String, String, Map<String, Object>> createLimiterHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

    @Value("${verify.limiter.rate.referer}")
    private String verifyReferer;

    private Log log = Log.getInstance(VerifyLimiterRateInterceptor.class);

    /**
     * 拦截条件 第一个是方法 第二个是类拦截
     */
    @Pointcut("(execution(@com.finace.miscroservice.commons.annotation.RateVerify * *(..))) || (within(@com.finace.miscroservice.commons.annotation.RateVerify *))")
    public void verifyInterceptor() {

    }

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    @Qualifier("limiterHashRedisTemplate")
    private HashOperations<String, String, Map<String, Object>> limiterHashRedisTemplate;


    private final String LIMITER_CURRENT_COUNT = "currentCount";

    private final String LIMITER_EXPIRE_TIME = "expireTime";

    /**
     * 微服方法的次数拦截
     *
     * @param joinPoint
     * @return
     */
    @Around("verifyInterceptor()&&@annotation(rateVerify)")
    public Object limiterRateInterceptor(ProceedingJoinPoint joinPoint, RateVerify rateVerify) throws Throwable {
        String id;
        String did;
        switch (rateVerify.interceptingMode()) {
            case HEADER:
                //获取字段
                id = request.getHeader(rateVerify.interceptingField());
                //验证字段
                if (StringUtils.isEmpty(id) || !UidUtils.isRegularUidByDevice(id, DeviceEnum.H5, DeviceEnum.IOS, DeviceEnum.ANDROID)) {
                    log.warn("Ip={} 所传的id={}字段不符合拦截={}规则", Iptools.gainRealIp(request), id, HEADER);
                    return getReturnType(joinPoint);
                }
                break;
            case BODY_PARAM:
                id = request.getParameter(rateVerify.interceptingField());
                if (!UidUtils.decryptUid(id, getKeyByFiled(rateVerify.interceptingField())).matches("[\\d:]+")) {
                    log.warn("Ip={} 所传的id={}字段不符合拦截={}规则", Iptools.gainRealIp(request), id, BODY_PARAM);
                    return getReturnType(joinPoint);
                }
                break;

            case PATH_PARAM:
                String uri = request.getRequestURI();
                id = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
                did = request.getParameter(rateVerify.interceptingField());

                if (StringUtils.isEmpty(id) || !UidUtils.isRegularUidByDevice(id, DeviceEnum.H5) || StringUtils.isEmpty(did)) {
                    log.warn("Ip={} 所传的id={}字段不符合拦截={}规则", Iptools.gainRealIp(request), id, PATH_PARAM);
                    return getReturnType(joinPoint);
                }

                //重新赋值
                id = id + did;


                break;
            default:
                log.warn("Ip={} 暂时不支持此方法的拦截", Iptools.gainRealIp(request));
                return getReturnType(joinPoint);
        }


        if (!rateVerify.allowedCrossDomain()) {
            String referer = request.getHeader("referer");
            if (referer == null || !referer.contains(verifyReferer)) {
                log.info("Ip={} 所传的Id={} 从其他网站={}进入被拦截", Iptools.gainRealIp(request), id, referer);
                return getReturnType(joinPoint);
            }
        }


        if (id == null) {
            log.warn("Ip={} 未从网关进入 被拦截", Iptools.gainRealIp(request));
            return getReturnType(joinPoint);
        }

        String uri = request.getRequestURI();
        //获取验证的单位
        Map<String, Object> expireVal = limiterHashRedisTemplate.get((RedisKeyEnum.LIMITER_HASH_REDIS_PREFIX_KEY.toKey() + uri).intern(), id);

        Integer currentCount = 0;
        Long expireTime = null;

        Boolean resetFlag = Boolean.TRUE;

        if (expireVal != null && !expireVal.isEmpty()) {

            //获取过期时间
            Long expireRecTime = (Long) expireVal.get(LIMITER_EXPIRE_TIME);

            if (expireRecTime == null) {
                //清除值
                limiterHashRedisTemplate.delete((RedisKeyEnum.LIMITER_HASH_REDIS_PREFIX_KEY.toKey() + uri).intern(), id);
                log.info("当前获取方法={} 限定参数为空", uri);
                return joinPoint.proceed();


            }

            //当前时间内 访问的次数是否超过最大值
            if (expireRecTime > System.currentTimeMillis()) {

                //获取当前次数
                Integer count = (Integer) expireVal.get(LIMITER_CURRENT_COUNT);

                //验证是否超过最大值
                if (count >= rateVerify.value()) {


                    log.warn("IP={} 下的用户id={} 访问={} 已经在规定时间={} 时间单位={} 超过最大次数={}",
                            Iptools.gainRealIp(request), id, uri, rateVerify.time(), rateVerify.timeUnit(), rateVerify.value());

                    //给端返回需要获取验证码
                    response.getWriter().write(JSONObject.toJSONString(Response.validate()));

                    return getReturnType(joinPoint);


                }

                //赋值给当前的次数
                currentCount = count;

                expireTime = expireRecTime;

                //不需要重新复制
                resetFlag = Boolean.FALSE;

            } else {
                log.info("Ip={} 下的用户id={} 访问={} 已经超过规定时间重新赋值", Iptools.gainRealIp(request), id, uri);
            }


        }

        //重新赋值
        if (resetFlag) {

            long expireInterval = 1;
            switch (rateVerify.timeUnit()) {
                case DAYS:
                    expireInterval = expireInterval * 24;
                case HOURS:
                    expireInterval = expireInterval * 60;
                case MINUTES:
                    expireInterval = expireInterval * 60;
                case SECONDS:
                    expireInterval = expireInterval * 1000;
                case MILLISECONDS:
                    expireInterval = 1 * expireInterval;
                    break;
                default:
                    log.error("暂时不支持其他类型的限定时间");
                    return getReturnType(joinPoint);
            }
            //没有值的时计算失效时间
            expireTime = System.currentTimeMillis() + expireInterval * rateVerify.time();

        }

        //赋值
        Map<String, Object> map = new HashMap<>();
        map.put(LIMITER_EXPIRE_TIME, expireTime);
        map.put(LIMITER_CURRENT_COUNT, ++currentCount);
        //存储
        limiterHashRedisTemplate.put((RedisKeyEnum.LIMITER_HASH_REDIS_PREFIX_KEY.toKey() + uri).intern(), id, map);

        return joinPoint.proceed();
    }


}
