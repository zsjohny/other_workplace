package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Ip的黑名单
 */
public class IpBlackList {

    private static final LoadingCache<String, Integer> IP_INTERCEPTOR_LIST = CacheBuilder.
            newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
        @Override
        public Integer load(String key) throws Exception {
            //返回失败
            return -1;
        }
    });

    private static Log log = Log.getInstance(IpBlackList.class);


    /**
     * 检查恶意Ip
     *
     * @param request
     * @return true是列入黑名单 false不是黑名单
     */
    public static boolean check(HttpServletRequest request) {

        boolean resultFlag = Boolean.FALSE;
        try {

            String ip = Iptools.gainRealIp(request);
            if (ip.isEmpty()) {
                log.warn("所传request 解析不出ip");
                return resultFlag;
            }

            Integer accessCount = IP_INTERCEPTOR_LIST.getUnchecked(ip);


            //因为缓存默认没有是-1
            if (accessCount>0) {
                log.info("ip={} 访问次数为={} 已被列入黑名单  30分钟内禁止访问", ip, accessCount);
                resultFlag = Boolean.TRUE;
                IP_INTERCEPTOR_LIST.put(ip, ++accessCount);
            }


        } catch (Exception e) {
            log.error("IP黑名单拦截检查失败", e);
        }
        return resultFlag;

    }


    /**
     * 设置黑名单
     *
     * @param ip
     */
    public static void setBlackList(String ip) {

        if (StringUtils.isEmpty(ip)) {
            log.warn("设置黑名单所传Ip为空");
            return;

        }
        //设定初始值
        IP_INTERCEPTOR_LIST.put(ip, 1);

    }

}
