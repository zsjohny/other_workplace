package com.goldplusgold.td.sltp.core.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.goldplusgold.td.sltp.core.auth.cache.ICacheService;
import com.goldplusgold.td.sltp.core.auth.constant.LoginParamsEnum;
import com.goldplusgold.td.sltp.core.auth.constant.StringConstant;
import com.goldplusgold.td.sltp.core.auth.constant.TokenInfo;
import com.goldplusgold.td.sltp.core.auth.data.IUserContext;
import com.goldplusgold.td.sltp.core.auth.data.UserContextUtils;
import com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException;
import com.goldplusgold.td.sltp.core.auth.exception.ResponseError;
import com.goldplusgold.td.sltp.core.auth.exception.TDJwtTokenException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static com.goldplusgold.td.sltp.core.auth.exception.TDErrorEnum.*;
import static com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException.Info.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * shiro拦截器(对app原生界面传过来的token进行处理,token与金专家方案一样)
 */
@Component
public class AppAuthcFilter extends UserFilter {

    private static final Logger logger = LoggerFactory.getLogger(AppAuthcFilter.class);

    @Autowired
    private IUserContext userContext;

    @Autowired
    private ICacheService<String> cacheService;

    @Autowired
    private MappingJackson2HttpMessageConverter jackson2Converter;

    @Value("${redis.session.liveTime}")
    private long sessionLiveTime;

    /**
     * 表示是否允许访问,如果返回 true 表示需要继续处理；如果返回 false表示该拦截器实例已经处理了，将直接返回即可；
     * 我们在这里验证jwt token是否有效，是否过期等
     * <p>
     * 从http header的authentication中取出Bearer Token信息，这里存储的就是jwt。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response,
                                      Object mappedValue) {

        boolean flag = false;
        String jwtToken = null;

        try {
            final HttpServletRequest req = WebUtils.toHttp(request);

            jwtToken = JwtUtils.getJWTTokenFromHeader(req);

            checkArgument(StringUtils.isNotEmpty(jwtToken));
            JwtUtils.verifyJWT(jwtToken, req);
            //验证token是否过期
            Long expir = (Long) req.getAttribute(TokenInfo.EXPIRATION);
            if (expir < new Date().getTime()) {
                throw new JwtTokenException(TD0002.toCode(), JZJ_TOKEN_EXPIRATION.toInfo(), TD0002.toInfo());
            }


            //从redis中获取td用户信息
            String jzjCustomerId = (String) req.getAttribute(LoginParamsEnum.JZJUSERID.toName());

            //验证金专家登录时间，如果金专家重新登录过，则清空TD缓存
            String jzjLoginTime = JwtUtils.getJZJLoginTimeStampFromHeader(req);
            String loginTimeStr = cacheService.get(String.format(StringConstant.JZJ_LOGIN_TIME, jzjCustomerId));
            if (StringUtils.isNotEmpty(jzjLoginTime)) {
                if (!jzjLoginTime.equals(loginTimeStr)) {
                    cacheService.set(String.format(StringConstant.JZJ_LOGIN_TIME, jzjCustomerId), jzjLoginTime);
                    cacheService.del(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId));
                    cacheService.del(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId));
                    throw new TDJwtTokenException(TD0004.toCode(),
                            TD_ACCOUNT_NOT_FOUND.toInfo(), TD0004.toInfo());
                }
            }

            String tdSessionId = cacheService.get(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId));
            String tdSessionKey = cacheService.get(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId));

            if (StringUtils.isEmpty(tdSessionId) || StringUtils.isEmpty(tdSessionKey)) {
                throw new TDJwtTokenException(TD0004.toCode(),
                        TD_ACCOUNT_NOT_FOUND.toInfo(), TD0004.toInfo());
            }
            String tdAccountNo = cacheService.get(String.format(StringConstant.TD_ACCT_NO, jzjCustomerId));
            String tdCustId = cacheService.get(String.format(StringConstant.TD_CUST_ID, jzjCustomerId));

            //重新设置sessionId,sessionKey的过期时间为10分钟
            cacheService.set(String.format(StringConstant.TD_SESSION_ID, jzjCustomerId), tdSessionId, sessionLiveTime);
            cacheService.set(String.format(StringConstant.TD_SESSION_KEY, jzjCustomerId), tdSessionKey, sessionLiveTime);


            // 把TD用户数据保存到request中，然后存入userContext.
            req.setAttribute(LoginParamsEnum.TDACCTNO.toName(), tdAccountNo);
            req.setAttribute(LoginParamsEnum.TDSESSIONID.toName(), tdSessionId);
            req.setAttribute(LoginParamsEnum.TDSESSIONKEY.toName(), tdSessionKey);
            req.setAttribute(LoginParamsEnum.TDCUSTID.toName(), tdCustId);

            UserContextUtils.set(req, userContext);
            flag = true;
        } catch (IllegalArgumentException e) {
            logger.error(JZJ_TOKEN_NOT_FOUND.toInfo(), e);
            ResponseError error = createResponseError(TD0001.toCode(),
                    TD0001.toInfo());
            request.setAttribute("error", error);
        } catch (JwtTokenException e) {
            logger.error(e.getErrorMsg());
            ResponseError error = createResponseError(e.getErrorCode(), e.getViewInfo());
            request.setAttribute("error", error);
        } catch (Exception e) {
            logger.error(JwtTokenException.Info.ACCESS_ERROR.toInfo(), e);
            ResponseError error = createResponseError(TD0005.toCode(),
                    TD0005.toInfo());
            request.setAttribute("error", error);
        }
        PrintJwtDebugLog.printDebugLog(request, flag, jwtToken);

        return flag;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {

        HttpServletRequest req = WebUtils.toHttp(request);
        ResponseError error = (ResponseError) req.getAttribute("error");
        writeErrorInfoToResponse(WebUtils.toHttp(response), error);
        return false;
    }


    private ResponseError createResponseError(String errorCode,
                                              String errorMsg) {

        ResponseError error = new ResponseError();
        error.setErrorCode(errorCode);
        error.setErrorMsg(errorMsg);
        return error;
    }


    private void writeErrorInfoToResponse(HttpServletResponse response,
                                          ResponseError error) throws JsonProcessingException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String errorJson = jackson2Converter.getObjectMapper().writeValueAsString(error);
        try (PrintWriter out = response.getWriter()) {
            out.write(errorJson);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
