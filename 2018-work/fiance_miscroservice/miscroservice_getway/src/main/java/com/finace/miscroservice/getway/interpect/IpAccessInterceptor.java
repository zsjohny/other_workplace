package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.config.IpLocalInterceptorConfig;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.getway.config.AccessInterceptorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip访问限制
 */
@Configuration
public class IpAccessInterceptor {


    private Log logger = Log.getInstance(IpAccessInterceptor.class);


    /**
     * 创建本地IP拦截的记录
     *
     * @return
     */
    @Bean
    public IpLocalInterceptorConfig createIpLocalInterceptorConfig() {
        return new IpLocalInterceptorConfig();
    }

    @Autowired
    private AccessInterceptorConfig accessInterceptorConfig;

    @Autowired
    private IpLocalInterceptorConfig ipLocalInterceptorConfig;

    /**
     * 验证拦截
     *
     * @param request 访问的请求
     * @return true 拦截成功 false拦截失败
     */
    public boolean interceptor(HttpServletRequest request) {

        boolean forbiddenFlag = Boolean.TRUE;
        String ip = Iptools.gainRealIp(request);

        try {


            /**
             * 限制本地的某个IP频率
             */
            if (!ipLocalInterceptorConfig.isAccess(request)) {
                logger.warn("IP={} 已经被禁止访问", ip);
                return forbiddenFlag;
            }


            /**
             * 限制所有的Ip的瞬间访问频率
             */
            if (!accessInterceptorConfig.checkPass()) {
                logger.warn("Ip ={} 瞬间访问超过频率被拦截", ip);
                return forbiddenFlag;
            }


            forbiddenFlag = Boolean.FALSE;


        } catch (Exception e) {
            logger.error("Ip={} 限制访问失败", ip, e);
            forbiddenFlag = Boolean.FALSE;
        }

        return forbiddenFlag;

    }
}
