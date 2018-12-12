package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.tools.MD5Util;
import com.hazelcast.core.IMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.finace.miscroservice.commons.utils.JwtToken.UID;
import static com.finace.miscroservice.getway.config.DistributeMapConfig.CACHE_LIVE_TIME;

/**
 * 请求一次的拦截 once+timestamp
 */
@Component
public class OnceReqInterceptor {

    @Autowired
    private IMap<Object, Object> cacheMap;

    @Value("${deviceVersion}")
    private String deviceVersion;


    private final int PERMIT_LEAST_TIME = 1000 * CACHE_LIVE_TIME;


    private final String TIMESTAMP_PARAM = "timestamp";
    private final String ONCE_PARAM = "once";
    private final String SIGN_PARAM = "sign";
    private final String VERSION_PARAM = "version";
    private final Pattern pattern = Pattern.compile("\\d+");

    private Log log = Log.getInstance(OnceReqInterceptor.class);


    private Boolean ERROR_FLAG = Boolean.FALSE;

    /**
     * 判断请求是否过期
     *
     * @param request 客户端的请求
     * @return true 过期 false没过期
     */
    public Boolean isInvalid(HttpServletRequest request) {
        Boolean invalidFlag = Boolean.FALSE;
        try {


            String version = request.getHeader(VERSION_PARAM);

            //version(低版本允许通过) | error
            if (ERROR_FLAG || version == null || deviceVersion.compareToIgnoreCase(version) > 0) {
                invalidFlag = Boolean.TRUE;
                log.info("Ip={} 方法={} 版本={} 允许通过", Iptools.gainRealIp(request), request.getRequestURI(), version);
                return invalidFlag;
            }


            String onceStr = request.getParameter(ONCE_PARAM);

            String timestampStr = request.getParameter(TIMESTAMP_PARAM);

            String signStr = request.getParameter(SIGN_PARAM);
            String uidStr = request.getHeader(UID);


            if (StringUtils.isAnyEmpty(onceStr, timestampStr, signStr)
                    || !pattern.matcher(timestampStr).matches()) {
                log.warn("once={} timestamp={} sign={}  所传入参数不符合规范"
                        , onceStr, timestampStr, signStr);
                return invalidFlag;
            }


            //once
            if (cacheMap.get(onceStr) != null) {
                log.warn("once={}已经被使用过,不给予通过", onceStr);
                return invalidFlag;
            }


            //timestamp
            Long interval = System.currentTimeMillis() - Long.parseLong(timestampStr);
            //判断时间是否超过规定时间
            if (interval > PERMIT_LEAST_TIME) {
                log.warn("传入时间差={}大于允许时间差={} 不给予通过", timestampStr, PERMIT_LEAST_TIME);
                return invalidFlag;
            }


            //sign
            //加密比对 uid+时间戳+once
            String signEncrypt = MD5Util.md5Hex(uidStr + timestampStr + onceStr);
            if (!signStr.equals(signEncrypt)) {
                log.warn("用户所传的sign={}与加密后sign={}的不正确,不给予通过", signStr, signEncrypt);
                return invalidFlag;
            }

            log.info("Ip={} once={} ,请求={} 验证通过", Iptools.gainRealIp(request), onceStr, request.getRequestURI());

            cacheMap.put(onceStr, uidStr);
        } catch (Exception e) {
            log.error("请求过期拦截出错,进行降级处理", e);
            ERROR_FLAG = Boolean.FALSE;
        }

        invalidFlag = Boolean.TRUE;
        return invalidFlag;
    }

}
