package com.goldplusgold.td.sltp.core.auth;

import com.goldplusgold.td.sltp.core.auth.cache.ICacheService;
import com.goldplusgold.td.sltp.core.auth.constant.LoginParamsEnum;
import com.goldplusgold.td.sltp.core.auth.constant.StringConstant;
import com.goldplusgold.td.sltp.core.auth.data.IUserContext;
import com.goldplusgold.td.sltp.core.auth.data.UserContextUtils;
import com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * shiro拦截器(针对app原生界面，处理匿名的情况，可能是登录过的，也可能是未登录过）
 */
@Component
public class AppAnonFilter extends AnonymousFilter {

    @Value("${redis.session.liveTime}")
    private long sessionLiveTime;

    @Autowired
    private ICacheService<String> cacheService;

    @Autowired
    private IUserContext userContext;
    private static final Logger logger = LoggerFactory.getLogger(AppAnonFilter.class);

    @Override
    protected boolean onPreHandle(ServletRequest request,
                                  ServletResponse response,
                                  Object mappedValue) {

        final HttpServletRequest req = WebUtils.toHttp(request);

        String jwtToken = JwtUtils.getJWTTokenFromHeader(req);
        if (StringUtils.isNotEmpty(jwtToken)) {
            try {
                JwtUtils.verifyJWT(jwtToken, req);
                //从redis中获取td用户信息
                String jzjCustomerId = (String) req.getAttribute(LoginParamsEnum.JZJUSERID.toName());

                //验证金专家登录时间，如果金专家重新登录过，则清空TD缓存
                String jzjLoginTime = JwtUtils.getJZJLoginTimeStampFromHeader(req);
                String loginTimeStr = cacheService.get(String.format(StringConstant.JZJ_LOGIN_TIME, jzjCustomerId));
                if(StringUtils.isNotEmpty(jzjLoginTime)){
                    if(!jzjLoginTime.equals(loginTimeStr)){
                        cacheService.set(String.format(StringConstant.JZJ_LOGIN_TIME, jzjCustomerId), jzjLoginTime);
                        cacheService.del(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId));
                        cacheService.del(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId));
                    }
                }

                String tdSessionId = cacheService.get(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId));
                String tdSessionKey = cacheService.get(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId));

                String tdAccountNo = cacheService.get(String.format(StringConstant.TD_ACCT_NO, jzjCustomerId));
                String tdCustId = cacheService.get(String.format(StringConstant.TD_CUST_ID, jzjCustomerId));

                //重新设置sessionId,sessionKey的过期时间为10分钟
                if(StringUtils.isNotEmpty(tdSessionId) && StringUtils.isNotEmpty(tdSessionKey)) {
                    cacheService.set(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId), tdSessionId, sessionLiveTime);
                    cacheService.set(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId), tdSessionKey, sessionLiveTime);

                    // 把TD用户数据保存到request中，然后存入userContext.
                    req.setAttribute(LoginParamsEnum.TDSESSIONID.toName(), tdSessionId);
                    req.setAttribute(LoginParamsEnum.TDSESSIONKEY.toName(), tdSessionKey);
                }

                if(StringUtils.isNotEmpty(tdAccountNo)){
                    req.setAttribute(LoginParamsEnum.TDACCTNO.toName(), tdAccountNo);
                    req.setAttribute(LoginParamsEnum.TDCUSTID.toName(), tdCustId);
                }
                UserContextUtils.set(req, userContext);
            } catch (JwtTokenException e) {
                logger.error(e.getErrorMsg(), e);
            }
        }
        return super.onPreHandle(request, response, mappedValue);
    }
}
